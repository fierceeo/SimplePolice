package com.voidcitymc.plugins.SimplePolice.events;

import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PoliceChat implements Listener {

    private static final TreeSet<String> playersWithToggledChat = new TreeSet<>();

    public static void setToggled(String uuid) {
        if (!playersWithToggledChat.contains(uuid)) {
            playersWithToggledChat.add(uuid);
        }
    }

    public static void removeToggled(String uuid) {
        playersWithToggledChat.remove(uuid);
    }

    public static boolean isToggled(String uuid) {
        return playersWithToggledChat.contains(uuid);
    }

    public static void toggleChat(String uuid) {
        if (isToggled(uuid)) {
            removeToggled(uuid);
        } else {
            setToggled(uuid);
        }
    }

    public static ArrayList<String> toggledList() {
        return new ArrayList<>(playersWithToggledChat);
    }

    //Event handlers
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (isToggled(event.getPlayer().getUniqueId().toString())) {
            event.setCancelled(true);
            event.getRecipients().clear();
            String playerName;
            if (ConfigValues.usePlayerDisplayNameInPoliceChat) {
                //use display name
                playerName = event.getPlayer().getDisplayName();
            } else {
                //use standard name
                playerName = event.getPlayer().getName();
            }

            for (int i = 0; i < toggledList().size(); i++) {
                if (Bukkit.getPlayer(UUID.fromString(toggledList().get(i))) != null) {
                    Bukkit.getPlayer(UUID.fromString(toggledList().get(i))).sendMessage(Messages.getMessage("PoliceChat", playerName, event.getMessage()));
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        toggledList().remove(event.getPlayer().getUniqueId().toString());
    }

}
