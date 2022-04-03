package me.sunnyreborn.pickupcontrol;

import java.io.File;
import java.util.Objects;

import me.sunnyreborn.pickupcontrol.commands.MainCommand;
import me.sunnyreborn.pickupcontrol.listeners.*;
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

	@Override
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
		regEvent(new ClickEvent());
	}

	private void regEvent(Listener l) {
		getServer().getPluginManager().registerEvents(l, this);
	}

	public static PickupControl getInstance() {
		return pl;
	}

}
