package me.sunnyreborn.pickupcontrol.gui;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.GUIITEM;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.utils.Others;
import me.sunnyreborn.pickupcontrol.utils.ShortString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.file.TempData;

public class GUI implements InventoryHolder {

	Inventory inv;

	public GUI(Player p, PickupControl pl) {
		TempData temp = pl.getData().getDataPlayer(p);
		inv = Bukkit.createInventory(this, ShortString.SIZE, ShortString.TITLE.replace("{MODE}", Others.modeToColor(temp.getMode())));

		for (ItemStack is : temp.getItems().values()) {
			inv.addItem(is);
		}

		ItemStack enable = Others.getGuiItem(GUIITEM.ENABLE);
		String[] enable_slot = Others.gui_slot.get(GUIITEM.ENABLE);
		
		ItemStack disable = Others.getGuiItem(GUIITEM.DISABLE);
		String[] disable_slot = Others.gui_slot.get(GUIITEM.DISABLE);

		ItemStack whitelist = Others.getGuiItem(GUIITEM.WHITELIST);
		String[] whitelist_slot = Others.gui_slot.get(GUIITEM.WHITELIST);

		ItemStack blacklist = Others.getGuiItem(GUIITEM.BLACKLIST);
		String[] blacklist_slot = Others.gui_slot.get(GUIITEM.BLACKLIST);

		if (temp.getToggle() == Toggle.ENABLE) {
			shortSetItem(enable, enable_slot);
		} else {
			shortSetItem(disable, disable_slot);
		}

		if (temp.getMode() == Mode.WHITELIST) {
			shortSetItem(whitelist, whitelist_slot);
		} else {
			shortSetItem(blacklist, blacklist_slot);
		}
	}

	private void shortSetItem(ItemStack is, String[] s) {
		if (s.length == 1) {
			inv.setItem(Integer.parseInt(s[0]), is);
		}
		for (int i = Integer.parseInt(s[0]); i <= Integer.parseInt(s[1]); i++) {
			inv.setItem(i, is);
		}
	}

	public void openInv(Player p) {
		p.openInventory(inv);
	}

	public Inventory getInventory() {
		return inv;
	}

}
