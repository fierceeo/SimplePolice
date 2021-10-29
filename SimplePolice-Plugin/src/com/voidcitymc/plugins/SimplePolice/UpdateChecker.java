package com.voidcitymc.plugins.SimplePolice;

import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    //Constants. Customize to your liking.
    private static final int ID = 73996; //The ID of your resource. Can be found in the resource URL.
    private static final String ERR_MSG = Messages.getMessage("UpdateCheckerFail");
    private static final String UPDATE_MSG = Messages.getMessage("UpdateCheckerNewUpdate", "&b https://www.spigotmc.org/resources/" + ID + "/updates");
    //PermissionDefault.FALSE == OPs need the permission to be notified.
    //PermissionDefault.TRUE == all OPs are notified regardless of having the permission.
    private static final Permission UPDATE_PERM = new Permission("police.update", PermissionDefault.OP);
    private static final long CHECK_INTERVAL = 12_000; //In ticks.
    private final JavaPlugin javaPlugin;
    private final String localPluginVersion;
    private String spigotPluginVersion;

    public UpdateChecker(final JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.localPluginVersion = javaPlugin.getDescription().getVersion();
    }

    public void checkForUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //The request is executed asynchronously as to not block the main thread.
                Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, () -> {
                    //Request the current version of your plugin on SpigotMC.
                    try {
                        final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ID).openConnection();
                        connection.setRequestMethod("GET");
                        spigotPluginVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                    } catch (final IOException e) {
                        Bukkit.getServer().getConsoleSender().sendMessage("Unable to check for a Simple Police update: please check your internet connection and firewall");
                        cancel();
                        return;
                    }

                    //Check if the requested version is the same as the one in your plugin.yml.
                    if (localPluginVersion.equals(spigotPluginVersion)) return;

                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', UPDATE_MSG));

                    //Register the PlayerJoinEvent
                    Bukkit.getScheduler().runTask(javaPlugin, () -> Bukkit.getPluginManager().registerEvents(new Listener() {
                        @EventHandler(priority = EventPriority.MONITOR)
                        public void onPlayerJoin(final PlayerJoinEvent event) {
                            final Player player = event.getPlayer();
                            if (!player.hasPermission(UPDATE_PERM)) return;
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', UPDATE_MSG));
                        }
                    }, javaPlugin));

                    cancel(); //Cancel the runnable as an update has been found.
                });
            }
        }.runTaskTimer(javaPlugin, 0, CHECK_INTERVAL);
    }
}
