package me.sunnyreborn.pickupcontrol.utils;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.file.DataManager;

public class ShortString {

    static PickupControl pl = PickupControl.getInstance();

    private static DataManager data = PickupControl.getInstance().getData();

    // GUI
    public static String TITLE = Others.color(data.getGuiYaml().getString("title"));
    public static String WHITELIST_NAME = Others.color(data.getGuiYaml().getString("items.whitelist.name"));
    public static String BLACKLIST_NAME = Others.color(data.getGuiYaml().getString("items.blacklist.name"));

    public static int SIZE = data.getGuiYaml().getInt("size");

    // Config
    public static String PUT_ON_WRONG_SLOT = Others.color(Others.color(pl.getConfig().getString("message.put-item-wrong-slot")));
    public static String WELCOME_MESSAGE = Others.color(Others.color(pl.getConfig().getString("message.welcome-message")));

}
