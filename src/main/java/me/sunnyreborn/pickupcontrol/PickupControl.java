package me.sunnyreborn.pickupcontrol;

import java.io.File;
import java.util.Objects;

import me.sunnyreborn.pickupcontrol.commands.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
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
		new File(getDataFolder() + "/data/");

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

	public void loadEvents() {
		getServer().getPluginManager().registerEvents(new PickupEvent(), this);
		getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		getServer().getPluginManager().registerEvents(new ClickEvent(), this);
	}

	public static PickupControl getInstance() {
		return pl;
	}

}
