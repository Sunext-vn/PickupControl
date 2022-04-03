package me.sunnyreborn.pickupcontrol.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.gui.GUI;

public class MainCommand implements CommandExecutor {

	PickupControl pl = PickupControl.getInstance();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player p))
			return true;

		if (!p.hasPermission("pickupcontrol.use")) return true;

		if (args.length == 0) {
			GUI gui = new GUI(p, pl);
			gui.openInv(p);
		}
		return true;
	}

}
