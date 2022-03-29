package me.sunnyreborn.pickupcontrol;

import java.io.File;
import java.util.Objects;

import me.sunnyreborn.pickupcontrol.commands.MainCommand;
import me.sunnyreborn.pickupcontrol.listeners.LegacyPickupEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.sunnyreborn.pickupcontrol.file.DataManager;
import me.sunnyreborn.pickupcontrol.listeners.ClickEvent;
import me.sunnyreborn.pickupcontrol.listeners.JoinEvent;
import me.sunnyreborn.pickupcontrol.listeners.PickupEvent;

@Getter
public class PickupControl extends JavaPlugin {

	private static PickupControl pl;

	private DataManager data;

	public void onEnable() {
		File dataFolder = new File(getDataFolder() + "/data/");
		if (!dataFolder.exists()) dataFolder.mkdirs();

		saveDefaultConfig();

		pl = this;

		data = new DataManager();

		Bukkit.getOnlinePlayers().forEach(all -> data.loadPlayer(all));

		loadEvents();

		Objects.requireNonNull(getServer().getPluginCommand("pickupcontrol")).setExecutor(new MainCommand());
	}

	public void onDisable() {
		data.getMap().clear();

		Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
	}

	private void loadEvents() {
		try {
			Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
			getServer().getPluginManager().registerEvents(new PickupEvent(), this);
		} catch (ClassNotFoundException e) {
			getServer().getPluginManager().registerEvents(new LegacyPickupEvent(), this);
		}
		getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		getServer().getPluginManager().registerEvents(new ClickEvent(), this);
	}

	public static PickupControl getInstance() {
		return pl;
	}

}
