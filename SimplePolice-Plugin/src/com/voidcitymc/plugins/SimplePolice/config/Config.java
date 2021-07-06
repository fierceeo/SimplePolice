package com.voidcitymc.plugins.SimplePolice.config;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Properties;

public class Config {
    private InputStream inputStream;
    private String configFileName = "SimplePolice.properties";
    private Properties configFileProperties;
    private String configFilePath = "."+File.separator+"plugins"+File.separator+"SimplePolice"+File.separator;
    private String configFileFullPath = "";

    public Config(String configFileName) {
        this.configFileName = configFileName;
    }

    public Config(String configFileName, String configFilePath) {
        this.configFileName = configFileName;
        this.configFilePath = configFilePath;
    }

    private boolean configExists() {
        return (new File(configFileFullPath)).exists();
    }

    public void setupConfig() {
        configFileFullPath = configFilePath+configFileName;
        (new File(configFilePath)).mkdirs();
        InputStream defaultConfig = Config.class.getResourceAsStream("/" + configFileName);
        Properties defaultConfigProperties = new Properties();

        try {
            defaultConfigProperties.load(defaultConfig);
        } catch (IOException exception) {
            System.out.println(ChatColor.RED+"Simple Police is unable to load internal config file - Stopping Server!");
            Bukkit.shutdown();
            return;
        }
        if (!configExists()) {
            try {
                (new File(configFileFullPath)).createNewFile();
                defaultConfigProperties.store(new FileOutputStream(configFileFullPath), "Simple Police\nDiscord Support: https://discord.gg/rxzHRHcC7W\nInformation explaining the config values can be found here: (insert github config link)");
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"Simple Police is unable to create a config file - Critical Error!");
            }
            configFileProperties = defaultConfigProperties;
            return;
        } else {
            try {
                inputStream = new FileInputStream(configFileFullPath);
            } catch (FileNotFoundException ignored) {
            }
            Properties localConfigFileProperties = new Properties();
            try {
                localConfigFileProperties.load(inputStream);
            } catch (IOException exception) {
                System.out.println(ChatColor.RED+"Simple Police is unable to load the config file - Critical Error!");
                configFileProperties = defaultConfigProperties;
                return;
            }

            if (!localConfigFileProperties.getProperty("BuildTimestamp").equals(defaultConfigProperties.getProperty("BuildTimestamp"))) {
                configFileProperties = updateConfig(localConfigFileProperties, defaultConfigProperties);
                try {
                    configFileProperties.store(new FileOutputStream(configFileFullPath), "Simple Police\nDiscord Support: https://discord.gg/rxzHRHcC7W\nInformation explaining the config values can be found here: (insert github config link)");
                } catch (IOException ignored) {
                }
            } else {
                configFileProperties = localConfigFileProperties;
            }
        }
    }

    private Properties updateConfig(Properties properties, Properties defaultConfigProperties) {
            Properties newConfigProperties = defaultConfigProperties;
            for (String currentKey: defaultConfigProperties.stringPropertyNames()) {
                String currentEntry = properties.getProperty(currentKey);
                if (currentEntry != null && !currentKey.equals("BuildTimestamp")) {
                    newConfigProperties.setProperty(currentKey, currentEntry);
                }
            }
            return newConfigProperties;
    }

    public String getConfigProperty(String property) {
        return configFileProperties.getProperty(property);
    }
}