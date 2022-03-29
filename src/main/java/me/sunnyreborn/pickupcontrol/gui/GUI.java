package me.sunnyreborn.pickupcontrol.gui;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.GuiItem;
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

		ItemStack enable = Others.getGuiItem(GuiItem.ENABLE);
		String[] enable_slot = pl.getData().gui_slot.get(GuiItem.ENABLE);

		ItemStack disable = Others.getGuiItem(GuiItem.DISABLE);
		String[] disable_slot = pl.getData().gui_slot.get(GuiItem.DISABLE);

		ItemStack whitelist = Others.getGuiItem(GuiItem.WHITELIST);
		String[] whitelist_slot = pl.getData().gui_slot.get(GuiItem.WHITELIST);

		ItemStack blacklist = Others.getGuiItem(GuiItem.BLACKLIST);
		String[] blacklist_slot = pl.getData().gui_slot.get(GuiItem.BLACKLIST);

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
			return;
		}
		for (int i = Integer.parseInt(s[0]); i <= Integer.parseInt(s[1]); ++i) {
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
