package com.voidcitymc.plugins.SimplePolice.frisk;

import com.voidcitymc.plugins.SimplePolice.LegacyUtils;
import com.voidcitymc.plugins.SimplePolice.Utility;
import com.voidcitymc.plugins.SimplePolice.apiInternals.EventManager;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Frisk implements Listener {
    private final FriskCooldownManager cooldownManager = new FriskCooldownManager();

    @EventHandler
    public void onFrisk(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player police = event.getPlayer();
            Player targetedPlayer = (Player) event.getRightClicked();
            if (ConfigValues.friskingEnabled && Utility.isPolice(police.getUniqueId().toString()) && LegacyUtils.getItemInMainHand(police.getInventory()).getType().equals(ConfigValues.friskStickMaterialType)) {
                long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldown(targetedPlayer.getUniqueId());

                if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= FriskCooldownManager.DEFAULT_COOLDOWN) {
                    cooldownManager.setCooldown(targetedPlayer.getUniqueId(), System.currentTimeMillis());


                    targetedPlayer.sendMessage(Messages.getMessage("FriskMSG"));
                    police.sendMessage(Messages.getMessage("FriskPolice", targetedPlayer.getName()));

                    PlayerInventory inventoryToFrisk = targetedPlayer.getInventory();

                    List<ItemStack> contents = Arrays.asList(inventoryToFrisk.getContents());

                    ArrayList<String> textToReturn = new ArrayList<>();

                    ArrayList<ItemStack> foundContrabandItems = new ArrayList<>();

                    for (ItemStack item: contents) {
                        if (item != null && Utility.isContraband(item)) {
                            if (Math.random() * 100 <= ConfigValues.percentOfFindingContraband) {
                                if (item.getItemMeta().getDisplayName() != null && !item.getItemMeta().getDisplayName().equals("")) {
                                    textToReturn.add(ChatColor.DARK_AQUA + "" + item.getAmount() + "x " + item.getItemMeta().getDisplayName());
                                } else {
                                    textToReturn.add(ChatColor.DARK_AQUA + "" + item.getAmount() + "x " + capitalize((item.getType().toString().replace("_", " ")).toLowerCase()));
                                }
                                foundContrabandItems.add(item);
                                targetedPlayer.getInventory().removeItem(item);
                                police.getInventory().addItem(item);
                            }
                        }
                    }

                    EventManager.runPlayerFriskEvent(police, targetedPlayer, foundContrabandItems.toArray(new ItemStack[foundContrabandItems.size()]));

                    if (!foundContrabandItems.isEmpty()) {
                        targetedPlayer.sendMessage(Messages.getMessage("FriskGuilty"));
                        targetedPlayer.sendMessage(textToReturn.toArray(new String[0]));
                    } else {
                        targetedPlayer.sendMessage(Messages.getMessage("FriskNotGuilty"));
                    }

                    police.sendMessage(Messages.getMessage("FriskPoliceAfterMsg"));
                    if (textToReturn.size() < 1) {
                        textToReturn.add(Messages.getMessage("FriskNoItems"));
                    }

                    police.sendMessage(textToReturn.toArray(new String[0]));

                } else {
                    police.sendMessage(Messages.getMessage("FriskTimeLeft", String.valueOf(FriskCooldownManager.DEFAULT_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeLeft))));
                }

            }

        }

    }

    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


}

class FriskCooldownManager {

    public static final int DEFAULT_COOLDOWN = ConfigValues.friskCooldown; //10 minutes default
    private final Map<String, Long> cooldowns = new HashMap<>();

    public void setCooldown(UUID player, long time) {
        if (time < 1) {
            cooldowns.remove(player.toString());
        } else {
            cooldowns.put(player.toString(), time);
        }
    }

    public long getCooldown(UUID player) {
        long l = 0;
        return cooldowns.getOrDefault(player.toString(), l);
    }
}
