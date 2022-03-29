package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.file.TempData;
import me.sunnyreborn.pickupcontrol.utils.Others;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class LegacyPickupEvent implements Listener {

    @EventHandler
    public void onPickupLegacy(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        ItemStack is = e.getItem().getItemStack().clone();
        is.setAmount(1);
        TempData temp = PickupControl.getInstance().getData().getDataPlayer(p);

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

}
