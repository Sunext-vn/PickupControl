package me.sunnyreborn.pickupcontrol.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack is;

    private ItemMeta meta;

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);

        meta = is.getItemMeta();
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;

        meta = is.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(Others.color(name));

        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        List<String> loreWithColor = new ArrayList<>();
        for(String s : lore) {
            loreWithColor.add(Others.color(s));
        }

        meta.setLore(loreWithColor);

        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> beforeLore = meta.getLore();
        if (meta.hasLore()) {
            assert beforeLore != null;
            beforeLore.addAll(lore);
            meta.setLore(beforeLore);
        } else {
            meta.setLore(lore);
        }

        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        return this;
    }

    public ItemBuilder setModelData(int data) {
        meta.setCustomModelData(data);

        return this;
    }

    public ItemStack buildItem() {
        is.setItemMeta(meta);

        return is;
    }

}
