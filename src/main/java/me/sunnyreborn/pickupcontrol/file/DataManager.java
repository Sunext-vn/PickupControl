package me.sunnyreborn.pickupcontrol.file;

import lombok.SneakyThrows;
import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.GuiItem;
import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.utils.Others;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class DataManager {

    PickupControl pl = PickupControl.getInstance();

    private final Map<Player, TempData> tempData = new HashMap<>();

    public Map<GuiItem, String[]> gui_slot = new HashMap<>();

    @SneakyThrows
    public YamlConfiguration getGuiYaml() {
        File gui = new File(pl.getDataFolder(), "gui.yml");
        if (!gui.exists()) {
            gui.getParentFile().mkdirs();
            pl.saveResource("gui.yml", false);
        }

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
        File file = new File(pl.getDataFolder() + "/data/" + p.getName() + ".yml");
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

        int count;

        if (yml.getConfigurationSection("items") == null) {
            count = 0;
        } else {
            count = Objects.requireNonNull(yml.getConfigurationSection("items")).getKeys(false).size();
        }

        yml.set("items." + count, finalItem);

        yml.save(file);

        data.getItems().putAll(getItems(p));
    }

    @SneakyThrows
    public void removeItems(Player p, int slot) {
        File file = getPlayer(p);
        TempData data = getDataPlayer(p);

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

        List<ItemStack> is = new ArrayList<>();

        yml.getConfigurationSection("items").getKeys(false).forEach(s -> is.add(yml.getItemStack("items." + s)));

        for (int i = slot; i <= is.size(); ++i) {
            if (i == is.size() - 1) {
                yml.set("items." + i, null);
                break;
            }
            yml.set("items." + i, is.get(i + 1));
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
