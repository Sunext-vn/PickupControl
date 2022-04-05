package me.sunnyreborn.pickupcontrol.utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.GuiItem;
import me.sunnyreborn.pickupcontrol.enums.Mode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Others {

    public static ItemStack getGuiItem(GuiItem guiItem) {
        YamlConfiguration gui = PickupControl.getInstance().getData().getGuiYaml();

        String path = "items." + guiItem.toString().toLowerCase();

        PickupControl.getInstance().getData().gui_slot.put(guiItem, gui.getString(path + ".slot").split(":"));

        Optional<XMaterial> material = XMaterial.matchXMaterial(Objects.requireNonNull(gui.getString(path + ".material")));

        if (material.isEmpty()) return new ItemStack(Material.DIRT, 1);

        Material m = material.get().parseMaterial();

        return new ItemBuilder(m, gui.getInt(path + ".amount"))
                .setName(gui.getString(path + ".name"))
                .setLore(gui.getStringList(path + ".lore"))
                .setGlow(gui.getBoolean(path + ".glow"))
                /*.setModelData(gui.getInt(path + ".model-id"))*/.buildItem();
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

    public static List<String> color(List<String> text) {
        List<String> result = new ArrayList<>();

        for (String s : text) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(s);

            while (matcher.find()) {
                String color = s.substring(matcher.start(), matcher.end());
                s = s.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(s);
            }
            result.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        return result;
    }

    public static String modeToColor(Mode mode) {
        if (mode == Mode.BLACKLIST) {
            return ShortString.BLACKLIST_NAME;
        } else {
            return ShortString.WHITELIST_NAME;
        }
    }

    public static boolean adminChecking(Player p) {
        return p.isOp() || p.hasPermission("pickupcontrol.admin");
    }

    public static void playSound(Player p, String sound) {
        Optional<XSound> xSound = XSound.matchXSound(sound);

        if (xSound.isEmpty()) return;

        Sound resultSound = xSound.get().parseSound();

        assert resultSound != null;
        p.playSound(p.getLocation(), resultSound, 1, 1);
    }
}

