package com.voidcitymc.plugins.SimplePolice.events;

import com.voidcitymc.plugins.SimplePolice.DatabaseUtility;
import com.voidcitymc.plugins.SimplePolice.Utility;
import com.voidcitymc.plugins.SimplePolice.apiInternals.EventManager;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//TODO: review code
public class Jail implements Listener {
    private static final HashMap<String, Double> sentenceLength = new HashMap<>();
    private static final HashMap<String, Long> cooldowns = new HashMap<>();
    public static final HashMap<String, Location> previousLocation = new HashMap<>();
    private static final HashMap<String, Integer> scheduledUnjails = new HashMap<>();
    private static final HashMap<String, String> playerJailMap = new HashMap<>();

    private static void setCooldown(UUID player, long time) {
        if (time < 1) {
            cooldowns.remove(player.toString());
        } else {
            cooldowns.put(player.toString(), time);
        }
    }

    private static long getCooldown(UUID player) {
        long l = 0;
        return cooldowns.getOrDefault(player.toString(), l);
    }

    public static ArrayList<UUID> jailedPlayers() {
        return (ArrayList<UUID>) playerJailMap.keySet().stream().map(string -> UUID.fromString(string)).collect(Collectors.toList());
    }

    private static boolean isJailTimeOver(UUID player) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getCooldown(player)) >= sentenceLength.get(player.toString());
    }

    public static String timeLeftMessage(int seconds) {
        String timeLeftText;

        if (seconds == 1) {
            timeLeftText = 1 + " " + Messages.getMessage("JailTimeUnitForTimeLeftEqual1");
        } else if (seconds < 60) {
            timeLeftText = seconds + " " + Messages.getMessage("JailTimeUnitForTimeLeftUnder60");
        } else if (seconds == 60) {
            timeLeftText = 1 + " " + Messages.getMessage("JailTimeUnitForTimeLeftEqual60");
        } else {
            timeLeftText = Math.round(((double) seconds) / 60) + " " + Messages.getMessage("JailTimeUnitForTimeLeftOver60");
        }
        return timeLeftText;
    }

    public static String getPlayerCurrentJail(UUID player) {
        return playerJailMap.get(player.toString());
    }

    public static double getSentenceLength(UUID player) {
        return sentenceLength.getOrDefault(player.toString(), 0.0);
    }

    public static double timeLeft(UUID player) {
        if (isJailed(player)) {
            return sentenceLength.get(player.toString()) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getCooldown(player));
        } else {
            return 0.0;
        }
    }

    public static boolean isJailed(UUID player) {
        if (!cooldowns.containsKey(player.toString()) && !sentenceLength.containsKey(player.toString())) {
            return false;
        } else if (isJailTimeOver(player)) {
            cooldowns.remove(player.toString());
            sentenceLength.remove(player.toString());
            return false;
        } else {
            return true;
        }
    }

    public static void jailPlayer(UUID player, Double jailTime) {
        Player bukkitPlayer = Bukkit.getPlayer(player);
        String jailName = getJail();
        if (bukkitPlayer != null) {
            if (!previousLocation.containsKey(player.toString())) {
                previousLocation.put(player.toString(), bukkitPlayer.getLocation());
                bukkitPlayer.teleport(getJailLocation(jailName));
            }
        }
        setCooldown(player, System.currentTimeMillis());
        sentenceLength.put(player.toString(), jailTime);

        if (scheduledUnjails.containsKey(player.toString())) {
            Bukkit.getScheduler().cancelTask(scheduledUnjails.get(player.toString()));
        }
        playerJailMap.put(player.toString(), jailName);
        int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SimplePolice"), () -> {
            unjailPlayer(player, true);
            playerJailMap.remove(player.toString());
            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage(Messages.getMessage("JailRelease"));
            }
        }, (long) (jailTime * 20));

        scheduledUnjails.put(player.toString(), id);

        EventManager.runPlayerJailEvent(Bukkit.getPlayer(player), jailName, jailTime);
    }

    public static void jailPlayer(UUID player, Double jailTime, String jailName) {
        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer != null && !previousLocation.containsKey(player.toString())) {
            previousLocation.put(player.toString(), bukkitPlayer.getLocation());
            bukkitPlayer.teleport(getJailLocation(jailName));
        }
        setCooldown(player, System.currentTimeMillis());
        sentenceLength.put(player.toString(), jailTime);

        if (scheduledUnjails.containsKey(player.toString())) {
            Bukkit.getScheduler().cancelTask(scheduledUnjails.get(player.toString()));
        }
        playerJailMap.put(player.toString(), jailName);
        int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SimplePolice"), () -> {
            unjailPlayer(player, true);
            playerJailMap.remove(player.toString());
            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage(Messages.getMessage("JailRelease"));
            }
        }, (long) (jailTime * 20));

        scheduledUnjails.put(player.toString(), id);

        EventManager.runPlayerJailEvent(Bukkit.getPlayer(player), jailName, jailTime);
    }

    public static void unjailPlayer(UUID player, boolean teleportBack) {
        cooldowns.remove(player.toString());
        sentenceLength.remove(player.toString());
        scheduledUnjails.remove(player.toString());
        playerJailMap.remove(player.toString());
        if (teleportBack && previousLocation.containsKey(player.toString())) {
            if (Bukkit.getPlayer(player) != null) {
                Objects.requireNonNull(Bukkit.getPlayer(player)).teleport(previousLocation.get(player.toString()));
            }
            previousLocation.remove(player.toString());
        }
        if (Bukkit.getPlayer(player) != null) {
            Bukkit.getPlayer(player).sendMessage(Messages.getMessage("JailRelease"));
        }

        EventManager.runPlayerUnjailEvent(Bukkit.getPlayer(player), playerJailMap.get(player.toString()), previousLocation.get(player.toString()));
    }

    public static Location getJailLocation(String jailName) {
        for (JailLocation jailLocation: DatabaseUtility.getJailLocations()) {
            if (jailLocation.getJailName().equals(jailName)) {
                return jailLocation.getLocation();
            }
        }
        return null;
    }

    public static String getJail() {
        ArrayList<String> jailsInUse = new ArrayList<>(playerJailMap.values());

        jailsInUse.addAll(Utility.jailList());

        return Utility.leastCommonElement(jailsInUse.toArray(new String[0]));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (isJailed(event.getPlayer().getUniqueId())) {
            //do not cancel death teleport
            if (!event.getTo().equals(getJailLocation(playerJailMap.get(event.getPlayer().getUniqueId().toString())))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Messages.getMessage("PlayerEscapeOutOfJail", Utility.timeUnit((int) timeLeft(event.getPlayer().getUniqueId()))));
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (isJailed(event.getEntity().getUniqueId())) {
            Player player = event.getEntity();
            player.spigot().respawn();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SimplePolice"), () -> {
                player.sendMessage(Messages.getMessage("PlayerEscapeOutOfJail", Utility.timeUnit((int) timeLeft(player.getUniqueId()))));
                player.teleport(getJailLocation(playerJailMap.get(player.getUniqueId().toString())));
            }, 1);

        }

    }

    public static class JailLocation {
        private String jailName;
        private Map<String, Object> location;

        public JailLocation(String jailName, Location location) {
            this.jailName = jailName;
            this.location = location.serialize();
        }

        public String getJailName() {
            return jailName;
        }

        public Location getLocation() {
            return Location.deserialize(location);
        }
    }

}
