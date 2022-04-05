package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PickupControl.getInstance().getData().getMap().remove(e.getPlayer());
    }

}
