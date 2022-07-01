package com.voidcitymc.plugins.SimplePolice.events;

import com.voidcitymc.plugins.SimplePolice.*;
import com.voidcitymc.plugins.SimplePolice.apiInternals.EventManager;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.gui.JailGUI;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PoliceListener implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attackerPlayer = (Player) event.getDamager();
            Player attackedPlayer = (Player) event.getEntity();

            if (!Utility.inSafeArea(attackedPlayer)) {
                if (ConfigValues.allowArrestsWithoutContraband || Utility.containsContraband(attackedPlayer.getInventory().getContents())) {
                    if (Utility.isPolice(attackerPlayer.getUniqueId().toString()) && (LegacyUtils.getItemInMainHand(attackerPlayer.getInventory()).getType() == ConfigValues.batonMaterialType)) {
                        if (Bukkit.getOnlinePlayers().contains(attackedPlayer)) {
                            if (!DatabaseUtility.getJailLocations().isEmpty()) {
                                attackedPlayer.sendMessage(Messages.getMessage("ArrestMsg"));

                                String jailName = Jail.getJail();

                                if (!Jail.isJailed(attackedPlayer.getUniqueId())) {
                                    if (attackedPlayer.isDead()) {
                                        attackedPlayer.spigot().respawn();
                                    }
                                    Jail.previousLocation.put(attackedPlayer.getUniqueId().toString(), attackedPlayer.getLocation());
                                    attackedPlayer.teleport(Jail.getJailLocation(jailName));
                                }

                                Utility.payPoliceOnArrest(attackerPlayer, attackedPlayer);
                                Utility.takeMoneyOnArrest(attackedPlayer);

                                JailGUI.onPlayerArrest(attackerPlayer, attackedPlayer, jailName);
                                EventManager.runPlayerArrestEvent(attackerPlayer, attackedPlayer, Jail.previousLocation.get(attackedPlayer.getUniqueId().toString()));
                                event.setCancelled(true);
                            } else {
                                attackerPlayer.sendMessage(Messages.getMessage("ArrestNoJails"));
                            }
                        } else {
                            attackerPlayer.sendMessage(Messages.getMessage("ArrestNPC"));
                        }
                    }
                } else {
                    attackerPlayer.sendMessage(Messages.getMessage("ArrestNoContraband"));
                }
            } else {
                attackerPlayer.sendMessage(Messages.getMessage("ArrestSafeArea"));
            }
        }
    }
}
