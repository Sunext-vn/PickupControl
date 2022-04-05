package me.sunnyreborn.pickupcontrol.utils;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.file.DataManager;

import java.util.List;

public class ShortString {

    static PickupControl pl = PickupControl.getInstance();

    private static DataManager data = PickupControl.getInstance().getData();

    // GUI
    public static String TITLE = Others.color(data.getGuiYaml().getString("title"));
    public static String WHITELIST_NAME = Others.color(data.getGuiYaml().getString("items.whitelist.name"));
    public static String BLACKLIST_NAME = Others.color(data.getGuiYaml().getString("items.blacklist.name"));
    public static String OPEN_SOUND = data.getGuiYaml().getString("open-sound");
    public static String CLOSE_SOUND = data.getGuiYaml().getString("close-sound");
    public static String CLICK_SOUND = data.getGuiYaml().getString("click-sound");

    public static List<String> ITEM_LORE = Others.color(data.getGuiYaml().getStringList("lore-for-items-to-remove"));

    public static int SIZE = data.getGuiYaml().getInt("size");

    // Config
    public static String WELCOME_MESSAGE = Others.color(pl.getConfig().getString("message.welcome-message"));

}
