package me.sunnyreborn.pickupcontrol.file;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class TempData {

	private Map<Integer, ItemStack> items;

	private Mode mode = Mode.BLACKLIST;
	
	private Toggle toggle = Toggle.ENABLE;

	public TempData(Map<Integer, ItemStack> items) {
		this.items = items;
	}

}
