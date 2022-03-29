package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.utils.Others;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.file.TempData;

public class PickupEvent implements Listener {

	PickupControl pl = PickupControl.getInstance();

	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if (!(e.getEntity() instanceof Player p))
			return;

		ItemStack is = e.getItem().getItemStack().clone();
		is.setAmount(1);
		TempData temp = pl.getData().getDataPlayer(p);

		if (temp.getToggle() == Toggle.DISABLE) return;

		if (temp.getItems().isEmpty())
			return;

		/* for (ItemStack file : temp.getItems().values()) {
			if (is.getType().equals(file.getType())) {
				replaceDurability(is, file);
			}
		} */

		if (temp.getItems().containsValue(is)) {
			if (temp.getMode() == Mode.BLACKLIST) {
				e.setCancelled(true);
			}
		} else {
			if (temp.getMode() == Mode.WHITELIST) {
				e.setCancelled(true);
			}
		}
	}

	// Check for Damage, still on developed
	/* private void replaceDurability(ItemStack floor, ItemStack file) {
		if (file.getItemMeta() instanceof Damageable) {
			Damageable tempFloor = (Damageable) floor.getItemMeta();
			Damageable tempFile = (Damageable) file.getItemMeta();
			
			int damaged = tempFile.getDamage();
			assert tempFloor != null;
			tempFloor.setDamage(damaged);
			
			floor.setItemMeta(tempFloor);
		}
	} */

}
