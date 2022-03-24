package me.sunnyreborn.pickupcontrol.file;

import java.io.File;
import java.util.*;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.SneakyThrows;

public class DataManager {

	private Map<Player, TempData> tempData = new HashMap<>();

	public YamlConfiguration getGuiYaml() {
		File gui = new File(PickupControl.getInstance().getDataFolder(), "gui.yml");
		return YamlConfiguration.loadConfiguration(gui);
	}

	//////////////////////////////////////////////////////////////

	public void loadPlayer(Player p) {
		TempData data = new TempData(getItems(p));

		data.setMode(getMode(p));
		data.setToggle(getToggle(p));

		tempData.put(p, data);
	}

	public TempData getDataPlayer(Player p) {
		if (!tempData.containsKey(p))
			loadPlayer(p);
		return tempData.get(p);
	}

	@SneakyThrows
	private File getPlayer(Player p) {
		File file = new File(PickupControl.getInstance().getDataFolder(), "/data/" + p.getName() + ".yml");
		if (!file.exists()) {
			file.createNewFile();

			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
			yml.set("mode", "WHITELIST");
			yml.set("toggle", "ENABLE");
			yml.save(file);
		}

		return file;
	}

	@SneakyThrows
	public void addItems(Player p, ItemStack is) {
		ItemStack finalItem = is.clone();
		finalItem.setAmount(1);
		if (existsItems(p, finalItem))
			return;

		File file = getPlayer(p);
		TempData data = getDataPlayer(p);

		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		int count = Objects.requireNonNull(yml.getConfigurationSection("items")).getKeys(false).size();

		yml.set("items." + count, finalItem);

		yml.save(file);

		data.getItems().putAll(getItems(p));
	}

	@SneakyThrows
	public void removeItems(Player p, int slot) {
		File file = getPlayer(p);
		TempData data = getDataPlayer(p);

		List<ItemStack> is = new ArrayList<>();

		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		yml.set("items." + slot, null);
		for (int i = slot; i <= Objects.requireNonNull(yml.getConfigurationSection("items")).getKeys(false).size(); i++) {
			if (i != slot) {
				is.add(yml.getItemStack("items." + i));
				yml.set("items." + i, null);
				yml.set("items." + (i - 1), is.get(i - 1));
			}
		}
		yml.save(file);

		data.getItems().clear();
		data.getItems().putAll(getItems(p));
	}

	private boolean existsItems(Player p, ItemStack is) {
		File file = getPlayer(p);

		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		if (!yml.contains("items"))
			return false;

		for (String s : Objects.requireNonNull(yml.getConfigurationSection("items")).getKeys(false)) {
			ItemStack self = yml.getItemStack("items." + s);
			if (is.equals(self)) {
				return true;
			}
		}
		return false;
	}

	private Map<Integer, ItemStack> getItems(Player p) {
		File file = getPlayer(p);
		Map<Integer, ItemStack> temp = new HashMap<>();

		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		if (!yml.contains("items"))
			return temp;

		for (String s : Objects.requireNonNull(yml.getConfigurationSection("items")).getKeys(false)) {
			ItemStack self = yml.getItemStack("items." + s);
			temp.put(Integer.valueOf(s), self);
		}

		return temp;
	}

	private Mode getMode(Player p) {
		File file = getPlayer(p);
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		return Mode.valueOf(yml.getString("mode"));
	}

	@SneakyThrows
	public void setMode(Player p, Mode mode) {
		File file = getPlayer(p);
		TempData temp = getDataPlayer(p);
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		temp.setMode(mode);
		yml.set("mode", mode.toString());

		yml.save(file);
	}
	
	private Toggle getToggle(Player p) {
		File file = getPlayer(p);
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		return Toggle.valueOf(yml.getString("toggle"));
	}

	@SneakyThrows
	public void setToggle(Player p, Toggle mode) {
		File file = getPlayer(p);
		TempData temp = getDataPlayer(p);
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

		temp.setToggle(mode);
		yml.set("toggle", mode.toString());

		yml.save(file);
	}

	public Map<Player, TempData> getMap() {
		return tempData;
	}

}
