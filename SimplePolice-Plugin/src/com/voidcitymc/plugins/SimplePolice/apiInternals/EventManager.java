package com.voidcitymc.plugins.SimplePolice.apiInternals;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import com.voidcitymc.plugins.SimplePolice.api.events.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EventManager {

    private static ArrayList<Player911Event> player911Events = new ArrayList<>();
    private static ArrayList<PlayerArrestEvent> playerArrestEvents = new ArrayList<>();
    private static ArrayList<PlayerFriskEvent> playerFriskEvents = new ArrayList<>();
    private static ArrayList<PlayerJailEvent> playerJailEvents = new ArrayList<>();
    private static ArrayList<PlayerUnjailEvent> playerUnjailEvents = new ArrayList<>();
    private static ArrayList<PoliceTeleportEvent> policeTeleportEvents = new ArrayList<>();

    public static boolean usingListenerApi = false;

    public static void registerEvent(SimplePoliceEvent event) {
        usingListenerApi = true;
        if (event instanceof PlayerArrestEvent) {
            playerArrestEvents.add((PlayerArrestEvent) event);
        } else if (event instanceof Player911Event) {
            player911Events.add((Player911Event) event);
        } else if (event instanceof PlayerFriskEvent) {
            playerFriskEvents.add((PlayerFriskEvent) event);
        } else if (event instanceof PlayerJailEvent) {
            playerJailEvents.add((PlayerJailEvent) event);
        } else if (event instanceof PlayerUnjailEvent) {
            playerUnjailEvents.add((PlayerUnjailEvent) event);
        } else if (event instanceof PoliceTeleportEvent) {
            policeTeleportEvents.add((PoliceTeleportEvent) event);
        }
    }

    public static void runPlayer911Event(Player caller, String message) {
        player911Events.forEach(event -> event.on911(caller, message));
    }

    public static void runPlayerArrestEvent(Player police, Player arrestedPlayer, Location arrestLocation) {
        playerArrestEvents.forEach(event -> event.onPlayerArrest(police, arrestedPlayer, arrestLocation));
    }

    public static void runPlayerFriskEvent(Player police, Player arrestedPlayer, ItemStack[] contrabandItemsFound) {
        playerFriskEvents.forEach(event -> event.onPlayerFrisk(police, arrestedPlayer, contrabandItemsFound));
    }

    public static void runPlayerJailEvent(Player arrestedPlayer, String jailName, double jailTime) {
        playerJailEvents.forEach(event -> event.onPlayerJail(arrestedPlayer, jailName, jailTime));
    }

    public static void runPlayerUnjailEvent(Player unjailedPlayer, String jailName, Location releaseLocation) {
        playerUnjailEvents.forEach(event -> event.onPlayerUnjail(unjailedPlayer, jailName, releaseLocation));
    }

    public static void runPoliceTp(Player police, Player targetedPlayer, Location teleportLocation) {
        policeTeleportEvents.forEach(event -> event.onPoliceTp(police, targetedPlayer, teleportLocation));
    }
}
