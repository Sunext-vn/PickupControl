package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.GuiItem;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.utils.Others;
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

import java.util.Arrays;
import java.util.List;

public class ClickEvent implements Listener {

	PickupControl pl = PickupControl.getInstance();

	/* Because of main Map store slot of GUI auto sort, it made the code gone wrong,
	   so I need to create this to make this code run right! */
	List<GuiItem> listGuiItem = Arrays.stream(GuiItem.values()).toList();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getView().getTopInventory().getHolder() instanceof GUI))
			return;

		int slot = e.getRawSlot();
		ItemStack press = e.getCursor();

		Player p = (Player) e.getWhoClicked();
		TempData data = pl.getData().getDataPlayer(p);
		
		if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
			e.setCancelled(true);
		}
		
		if (slot >= 0 && slot <= 53) {
			e.setCancelled(true);

			if (press != null && press.getType() != Material.AIR) {
				pl.getData().addItems(p, press);
				p.getInventory().addItem(press);
				press.setAmount(0);
				GUI gui = new GUI(p, pl);
				gui.openInv(p);
				return;
			}

			GuiItem clickRecognizes = clickSlot(slot);

			if (clickRecognizes == GuiItem.BLACKLIST || clickRecognizes == GuiItem.WHITELIST) {
				toggleMode(p);
				GUI gui = new GUI(p, pl);
				gui.openInv(p);
				return;
			}

			if (clickRecognizes == GuiItem.ENABLE || clickRecognizes == GuiItem.DISABLE) {
				toggleToggle(p);
				GUI gui = new GUI(p, pl);
				gui.openInv(p);
			}
		}
		
		if (data.getItems().isEmpty()) return;

		if (data.getItems().containsKey(slot)) {
			pl.getData().removeItems(p, slot);
			GUI gui = new GUI(p, pl);
			gui.openInv(p);
		}
	}

	private void toggleMode(Player p) {
		TempData temp = pl.getData().getDataPlayer(p);

		if (temp.getMode() == Mode.WHITELIST) {
			pl.getData().setMode(p, Mode.BLACKLIST);
		} else {
			pl.getData().setMode(p, Mode.WHITELIST);
		}
	}
	
	private void toggleToggle(Player p) {
		TempData temp = pl.getData().getDataPlayer(p);

		if (temp.getToggle() == Toggle.ENABLE) {
			pl.getData().setToggle(p, Toggle.DISABLE);
		} else {
			pl.getData().setToggle(p, Toggle.ENABLE);
		}
	}

	private GuiItem clickSlot(int slot) {
		for (GuiItem guiItem : listGuiItem) {
			String[] s = pl.getData().gui_slot.get(guiItem);

				if (s.length == 1) {
					if (slot == Integer.parseInt(s[0])) {
						return guiItem;
					}
					continue;
				}
				for (int i = Integer.parseInt(s[0]); i <= Integer.parseInt(s[1]); ++i) {
					if (slot == i) {
						return guiItem;
					}
				}
		}
		return null;
	}

}
