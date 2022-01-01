package com.voidcitymc.plugins.SimplePolice;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceAPI;
import com.voidcitymc.plugins.SimplePolice.api.Utility;
import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import com.voidcitymc.plugins.SimplePolice.apiInternals.EventManager;
import com.voidcitymc.plugins.SimplePolice.cmd.NineOneOne;
import com.voidcitymc.plugins.SimplePolice.cmd.Police;
import com.voidcitymc.plugins.SimplePolice.config.Config;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import com.voidcitymc.plugins.SimplePolice.events.PoliceChat;
import com.voidcitymc.plugins.SimplePolice.events.PoliceListener;
import com.voidcitymc.plugins.SimplePolice.events.TabComplete;
import com.voidcitymc.plugins.SimplePolice.frisk.Frisk;
import com.voidcitymc.plugins.SimplePolice.gui.JailGUI;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import com.voidcitymc.plugins.SimplePolice.metrics.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplePolice extends JavaPlugin implements SimplePoliceAPI {

    private static Metrics metrics;

    protected static Economy economy = null;

    public static boolean vaultInstalled = false;
    public static boolean qaInstalled = false;

    private static String pluginFolderPath;

    @Override
    public void onEnable() {
        pluginFolderPath = getDataFolder().getAbsolutePath();
        vaultInstalled = setupEconomy();
        qaInstalled = (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null);
        Config config = new Config("SimplePolice.properties");
        config.setupConfig();
        ConfigValues.initialize(config);
        Config messagesConfig = new Config("Messages.properties");
        messagesConfig.setupConfig();
        Messages.initialize(messagesConfig);
        DatabaseUtility.initDatabase();

        (new UpdateChecker(this)).checkForUpdate();
        metrics = new Metrics(this, 6814);
        registerEvents();
        System.out.println("Simple Police has been enabled!");
    }

    @Override
    public void onDisable() {
        metrics.addCustomChart(new Metrics.SimplePie("use_listener_api", () -> String.valueOf(EventManager.usingListenerApi)));
        System.out.println("Simple Police has been disabled");
        System.out.println("-- Saving Data --");
        DatabaseUtility.save();
        System.out.println("-- All data saved! --");
    }

    public static String getPluginFolderPath() {
        return pluginFolderPath;
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new JailGUI(), this);
        getServer().getPluginManager().registerEvents(new PoliceListener(), this);
        getServer().getPluginManager().registerEvents(new Frisk(), this);
        getServer().getPluginManager().registerEvents(new Jail(), this);
        getServer().getPluginManager().registerEvents(new PoliceChat(), this);
        getServer().getPluginManager().registerEvents(new TabComplete(), this);
        getCommand("police").setExecutor(new Police());
        getCommand("911").setExecutor(new NineOneOne());
    }

    @Override
    public void registerEvent(SimplePoliceEvent event) {
        EventManager.registerEvent(event);
    }

    @Override
    public Utility getUtilityInstance() {
        return new com.voidcitymc.plugins.SimplePolice.apiInternals.Utility();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}
