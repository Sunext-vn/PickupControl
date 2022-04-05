package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.gui.GUI;
import me.sunnyreborn.pickupcontrol.utils.Others;
import me.sunnyreborn.pickupcontrol.utils.ShortString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseGuiEvent implements Listener {

    @EventHandler
    public void onCloseGui(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof GUI) Others.playSound((Player) e.getPlayer(), ShortString.CLOSE_SOUND);
    }

}
