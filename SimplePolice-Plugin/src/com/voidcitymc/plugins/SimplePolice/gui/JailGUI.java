package com.voidcitymc.plugins.SimplePolice.gui;

import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import com.voidcitymc.plugins.SimplePolice.Utility;
import com.voidcitymc.plugins.SimplePolice.LegacyUtils;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class JailGUI implements Listener {

    private static final String guiName = "Select Jail Time";
    public static HashMap<String, String> lastArrest = new HashMap<>();
    public static HashMap<String, String> currentJail = new HashMap<>();

    @EventHandler
    public void gui(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTitle().equalsIgnoreCase(guiName)) {
            event.setCancelled(true);

            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                double jailTime = ConfigValues.jailGUITimes[event.getRawSlot()-2];

                UUID jailedPlayer = UUID.fromString(lastArrest.get(player.getUniqueId().toString()));
                player.sendMessage(Messages.getMessage("JailTimePoliceMSG", Bukkit.getPlayer(jailedPlayer).getName(), String.valueOf(jailTime)));

                Player bukkitJailedPlayer = Bukkit.getPlayer(jailedPlayer);
                if (bukkitJailedPlayer != null) {
                    if (!Jail.isJailed(jailedPlayer)) {
                        bukkitJailedPlayer.sendMessage(Messages.getMessage("JailTimeMSG", String.valueOf(jailTime)));
                    } else {
                        bukkitJailedPlayer.sendMessage(Messages.getMessage("JailTimeChange", Utility.timeUnit((int) jailTime)));
                    }
                }

                Jail.jailPlayer(jailedPlayer, jailTime * 60, currentJail.get(jailedPlayer.toString()));
                currentJail.remove(jailedPlayer.toString());
                lastArrest.remove(player.getUniqueId().toString());
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void preventShiftgui(InventoryMoveItemEvent event) {
        if ((event.getSource().getHolder() instanceof Player) && ((Player) event.getSource().getHolder()).getOpenInventory().getTitle().equalsIgnoreCase(guiName)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase(guiName) &&
                lastArrest.containsKey(event.getPlayer().getUniqueId().toString())) {
            openInventory((Player) event.getPlayer());
        }
    }


    public void openInventory(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SimplePolice"), () -> player.openInventory(createGUI(player)), 1L);

    }


    public Inventory createGUI(Player player) {
        Inventory guiInventory = Bukkit.createInventory(null, 9, guiName);

        ItemStack[] guiItemstackList = new ItemStack[]{
                LegacyUtils.getStainedClay(DyeColor.RED),
                LegacyUtils.getStainedClay(DyeColor.ORANGE),
                LegacyUtils.getStainedClay(DyeColor.YELLOW),
                LegacyUtils.getStainedClay(DyeColor.LIME),
                LegacyUtils.getStainedClay(DyeColor.LIGHT_BLUE)};

        double[] jailGUITimes = ConfigValues.jailGUITimes;
        for (int i = 2; i < 7; i++) {
            guiInventory.setItem(i, Utility.createGuiItem(guiItemstackList[i-2], Messages.getMessage("JailGUIBlock", String.valueOf(jailGUITimes[i-2]))));
        }




        return guiInventory;
    }

    public static void onPlayerArrest(Player police, Player arrestedPlayer, String jailName) {
        (new JailGUI()).openInventory(police);
        lastArrest.put(police.getUniqueId().toString(), arrestedPlayer.getUniqueId().toString());
        currentJail.put(arrestedPlayer.getUniqueId().toString(), jailName);
    }
}
