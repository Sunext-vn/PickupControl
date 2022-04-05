package me.sunnyreborn.pickupcontrol.listeners;

import me.sunnyreborn.pickupcontrol.PickupControl;
import me.sunnyreborn.pickupcontrol.enums.ClickNode;
import me.sunnyreborn.pickupcontrol.enums.GuiItem;
import me.sunnyreborn.pickupcontrol.enums.Toggle;
import me.sunnyreborn.pickupcontrol.utils.Others;
import me.sunnyreborn.pickupcontrol.utils.ShortString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.sunnyreborn.pickupcontrol.enums.Mode;
import me.sunnyreborn.pickupcontrol.file.TempData;
import me.sunnyreborn.pickupcontrol.gui.GUI;

import java.util.Arrays;
import java.util.List;

public class ClickEvent implements Listener {

    PickupControl pl = PickupControl.getInstance();

    /* Because of main Map store slot of GUI auto sort, it made the code gone wrong,
       so I need to create this to make this code run correctly! */
    List<GuiItem> listGuiItem = Arrays.stream(GuiItem.values()).toList();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getView().getTopInventory().getHolder() instanceof GUI))
            return;

        int slot = e.getRawSlot();
        ItemStack press = e.getCurrentItem();

        Player p = (Player) e.getWhoClicked();
        TempData data = pl.getData().getDataPlayer(p);

        Others.playSound(p, ShortString.CLICK_SOUND);

        e.setCancelled(true);

        if (slot >= 0 && slot <= 53) {
            GuiItem clickRecognizes = clickSlot(slot);

            if (clickRecognizes == GuiItem.BLACKLIST || clickRecognizes == GuiItem.WHITELIST) {
                clickAction(ClickNode.MODE, p, null, 0);
                return;
            }

            if (clickRecognizes == GuiItem.ENABLE || clickRecognizes == GuiItem.DISABLE) {
                clickAction(ClickNode.TOGGLE, p, null, 0);
            }

            if (data.getItems().isEmpty()) return;

            if (data.getItems().containsKey(slot))
                clickAction(ClickNode.REMOVE, p, null, slot);
        }

        if (slot >= 54 && slot <= 89)
            clickAction(ClickNode.ADD, p, press, slot);
    }

    private void toggleMode(Player p) {
        TempData temp = pl.getData().getDataPlayer(p);

        if (temp.getMode() == Mode.WHITELIST) {
            pl.getData().setMode(p, Mode.BLACKLIST);
            return;
        }
        pl.getData().setMode(p, Mode.WHITELIST);
    }

    private void toggleToggle(Player p) {
        TempData temp = pl.getData().getDataPlayer(p);

        if (temp.getToggle() == Toggle.ENABLE) {
            pl.getData().setToggle(p, Toggle.DISABLE);
            return;
        }
        pl.getData().setToggle(p, Toggle.ENABLE);
    }

    private void clickAction(ClickNode clickNode, Player p, ItemStack press, int slot) {
        switch (clickNode) {
            case MODE -> toggleMode(p);
            case TOGGLE -> toggleToggle(p);
            case ADD -> pl.getData().addItems(p, press);
            case REMOVE -> pl.getData().removeItems(p, slot);
        }
        GUI gui = new GUI(p, pl);
        gui.openInv(p);
    }

    /* Check which slot corresponding to their GuiItem */
    private GuiItem clickSlot(int slot) {
        for (GuiItem guiItem : listGuiItem) { // Get GuiItem and check one by one to get the correct slot corresponding to them
            String[] s = pl.getData().gui_slot.get(guiItem); // Get slot values from gui_slot at DataManager.class

            if (s.length == 1) { // Check if that array of value is only 1 string or not
                if (slot == Integer.parseInt(s[0])) { // Then, check if -slot- provided above is equals to slot value or not
                    return guiItem; // If so, return the GuiItem object which have slot value equals -slot-
                }
                continue; // Go on if -slot- not equals slot value
            }
            /* If -s- not have only 1 string, we will continue to check 2 values it have
             * About why I check only 2, because of items.-GuiItem-.slot in gui.yml
             * is only support 2 types of value: slot and slot:slot
             * Ex: "45" or "45:49"
             *
             * "45:49" mean from slot 45 to slot 49
             *
             * Made a loop start at value 0 and end at value 1
             * ++i in For loop is faster than i++ if you want to ask
             *
             * It faster from 5 to 10 milliseconds. Well, it doesn't make a lot of big changes, but I like it */
            for (int i = Integer.parseInt(s[0]); i <= Integer.parseInt(s[1]); ++i) {
                if (slot == i) { // Check if value from loop equals to -slot- or not
                    return guiItem; // If so, return the GuiItem object which have slot value equals -slot-
                }
            }
        }
        return null;
    }

}
