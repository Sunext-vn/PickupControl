package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.utils.Others;
import me.sunnyreborn.pickupcontrol.utils.ShortString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.file.TempData;
import me.sunnyreborn.pickupcontrol.gui.GUI;

public class ClickEvent implements Listener {

	PickupControl pl = PickupControl.getInstance();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getView().getTopInventory().getHolder() instanceof GUI))
			return;

		int slot = e.getRawSlot();
		ItemStack press = e.getCursor();

		Player p = (Player) e.getWhoClicked();
		TempData data = pl.getData().getDataPlayer(p);
		
		if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			p.sendMessage(ShortString.PUT_ON_WRONG_SLOT);
			e.setCancelled(true);
		}
		
		if (slot >= 0 && slot <= 53) e.setCancelled(true);

		if (slot == 49) {
			toggleMode(p);
			GUI gui = new GUI(p, pl);
			gui.openInv(p);
		}
		
		if (slot >= 45 && slot <= 53 && slot != 49) {
			toggleToggle(p);
			GUI gui = new GUI(p, pl);
			gui.openInv(p);
		}
		
		if (slot >= 0 && slot <= 44) {
			if (press != null && press.getType() != Material.AIR) {
				pl.getData().addItems(p, press);
				p.getInventory().addItem(press);
				press.setAmount(0);
				GUI gui = new GUI(p, pl);
				gui.openInv(p);
				return;
			}
		}
		
		if (data.getItems().isEmpty()) return;

		if (data.getItems().containsKey(slot)) {
			pl.getData().removeItems(p, slot);
			GUI gui = new GUI(p, pl);
			gui.openInv(p);
		}
	}

	public void toggleMode(Player p) {
		TempData temp = pl.getData().getDataPlayer(p);

		if (temp.getMode() == Mode.WHITELIST) {
			pl.getData().setMode(p, Mode.BLACKLIST);
		} else {
			pl.getData().setMode(p, Mode.WHITELIST);
		}
	}
	
	public void toggleToggle(Player p) {
		TempData temp = pl.getData().getDataPlayer(p);

		if (temp.getToggle() == Toggle.ENABLE) {
			pl.getData().setToggle(p, Toggle.DISABLE);
		} else {
			pl.getData().setToggle(p, Toggle.ENABLE);
		}
	}

}
