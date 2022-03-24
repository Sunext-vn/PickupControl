package me.sunnyreborn.pickupcontrol.utils;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.GUIITEM;
import me.sunnyreborn.pickupcontrol.enums.Mode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Others {

    public static Map<GUIITEM, String[]> gui_slot = new HashMap<>();

    public static ItemStack getGuiItem(GUIITEM guiItem) {
        YamlConfiguration gui = PickupControl.getInstance().getData().getGuiYaml();

        String path = "items" + guiItem.toString().toLowerCase();

        gui_slot.put(guiItem, Objects.requireNonNull(gui.getString(path + "slot")).split(":"));

        return new ItemBuilder(Material.getMaterial(Objects.requireNonNull(gui.getString(path + ".material"))),
                gui.getInt(path + ".amount"))
                .setName(gui.getString(path + ".name"))
                .setLore(gui.getStringList(path + ".lore"))
                .setGlow(gui.getBoolean(path + ".glow"))
                .setModelData(gui.getInt(path + ".model-id")).buildItem();
    }

    public static String color(String text) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(text);
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }


    public static String modeToColor(Mode mode) {
        if (mode == Mode.BLACKLIST) {
            return ShortString.BLACKLIST_NAME;
        } else {
            return ShortString.WHITELIST_NAME;
        }
    }
}

