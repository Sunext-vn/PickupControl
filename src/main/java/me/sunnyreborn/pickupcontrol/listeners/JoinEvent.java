package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.file.TempData;
import me.sunnyreborn.pickupcontrol.utils.Others;
import me.sunnyreborn.pickupcontrol.utils.ShortString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

	PickupControl pl = PickupControl.getInstance();

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		pl.getData().loadPlayer(p);

		TempData temp = pl.getData().getMap().get(p);
		if (temp.getToggle() == Toggle.ENABLE)
			p.sendMessage(ShortString.WELCOME_MESSAGE.replace("{MODE}",  Others.modeToColor(temp.getMode())));
	}

}
