package me.sunnyreborn.pickupcontrol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.sunnyreborn.pickupcontrol.commands.MainCommand;
import me.sunnyreborn.pickupcontrol.listeners.*;
import me.sunnyreborn.pickupcontrol.utils.Others;
import me.sunnyreborn.pickupcontrol.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.sunnyreborn.pickupcontrol.file.DataManager;

@Getter
public class PickupControl extends JavaPlugin {

	private static PickupControl pl;

	private DataManager data;

	public List<String> updateMessage = new ArrayList<>();

	@Override
	public void onEnable() {
		File dataFolder = new File(getDataFolder() + "/data/");
		if (!dataFolder.exists()) dataFolder.mkdirs();

		saveDefaultConfig();

		pl = this;

		data = new DataManager();

		checkUpdate();

		Bukkit.getOnlinePlayers().forEach(all -> data.loadPlayer(all));

		loadEvents();

		Objects.requireNonNull(getServer().getPluginCommand("pickupcontrol")).setExecutor(new MainCommand());
	}

	@Override
	public void onDisable() {
		data.getMap().clear();

		Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
	}

	private void loadEvents() {
		try {
			Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
			regEvent(new PickupEvent());
		} catch (ClassNotFoundException e) {
			regEvent(new LegacyPickupEvent());
		}
		regEvent(new JoinEvent());
		regEvent(new QuitEvent());
		regEvent(new ClickEvent());
	}

	private void regEvent(Listener l) {
		getServer().getPluginManager().registerEvents(l, this);
	}

	private void checkUpdate() {
		new UpdateChecker(101154).getVersion(version -> {
			if (this.getDescription().getVersion().equals(version)) {
				updateMessage.add(Others.color("&7[&cPickupControl&7] &aThere is not a new update available."));
			} else {
				updateMessage.add(Others.color("&7[&cPickupControl&7] §7The plugin version you are using is §4out of date§7!"));
				updateMessage.add(Others.color("&7[&cPickupControl&7] §7There is a new update available."));
				updateMessage.add(Others.color("&7[&cPickupControl&7] §7Download it here: §6https://bom.so/63uV5V"));
			}

			updateMessage.forEach(s -> Bukkit.getConsoleSender().sendMessage(s));
			Bukkit.getOnlinePlayers().forEach(p -> {
				if (Others.adminChecking(p)) updateMessage.forEach(p::sendMessage);
			});
		});
	}

	public static PickupControl getInstance() {
		return pl;
	}

}
