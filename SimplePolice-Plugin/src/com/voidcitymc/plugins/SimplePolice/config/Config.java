package com.voidcitymc.plugins.SimplePolice.config;

import com.voidcitymc.plugins.SimplePolice.SimplePolice;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {
    private final String fileName;
    private final String fileFullPath;

    private final String headerComment = "Simple Police\nDiscord Support: https://discord.gg/rxzHRHcC7W\nInformation explaining the config values can be found here: https://github.com/fierceeo/SimplePolice/tree/master/SimplePolice-Plugin/src/main/resources";

    private Properties fileProperties;

    public Config(String fileName) {
        this.fileName = fileName;
        fileFullPath = SimplePolice.getPluginFolderPath() + File.separator + fileName;
    }

    public void setupConfig() {
        (new File(SimplePolice.getPluginFolderPath())).mkdirs();

        Properties defaultFileProperties = new Properties();
        fileProperties = new Properties();

        if (!load(defaultFileProperties, "/" + fileName, true)) {
            System.out.println(ChatColor.RED+"Simple Police is unable to load file from jar: "+ fileName +" - Stopping Server!");
            Bukkit.shutdown();
            return;
        }

        if (!fileExists(fileFullPath)) {
            if (!createEmptyFile(fileFullPath)) {
                System.out.println(ChatColor.RED + "Simple Police is unable to create file: " + fileFullPath + " - Stopping Server!");
                Bukkit.shutdown();
                return;
            }
            if (!save(defaultFileProperties, fileFullPath, headerComment)) {
                System.out.println(ChatColor.RED+"Simple Police is unable to write to file: "+ fileFullPath +" - Stopping Server!");
                Bukkit.shutdown();
                return;
            }
            fileProperties = defaultFileProperties;
            return;
        }

        if (!load(fileProperties, fileFullPath, false)) {
            System.out.println(ChatColor.RED+"Simple Police is unable to load file from filesystem: "+ fileFullPath +" - Stopping Server!");
            Bukkit.shutdown();
            return;
        }

        if (!fileProperties.getProperty("BuildTimestamp").equals(defaultFileProperties.getProperty("BuildTimestamp"))) {
            updateConfig(fileProperties, defaultFileProperties);
        }

        if (!save(fileProperties, fileFullPath, headerComment)) {
            System.out.println(ChatColor.RED+"Simple Police is unable to write to file: "+ fileFullPath +" - Stopping Server!");
            Bukkit.shutdown();
            return;
        }
    }

    private void updateConfig(Properties properties, Properties defaultConfigProperties) {
        for (String currentKey: defaultConfigProperties.stringPropertyNames()) {
            if (!properties.containsKey(currentKey) || currentKey.equals("BuildTimestamp")) {
                properties.setProperty(currentKey, defaultConfigProperties.getProperty(currentKey));
            }
        }
    }

    public String getProperty(String property) {
        return fileProperties.getProperty(property);
    }

    private boolean fileExists(String path) {
        return (new File(path)).exists();
    }

    private boolean createEmptyFile(String path) {
        try {
            (new File(path)).createNewFile();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean save(Properties propertiesFile, String path, String comments) {
        try {
            FileOutputStream fileStream = new FileOutputStream(path);
            propertiesFile.store(new OutputStreamWriter(fileStream, StandardCharsets.UTF_8), comments);
            fileStream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean load(Properties propertiesFile, String path, boolean fileInJar) {
        InputStream fileStream;
        if (fileInJar) {
            fileStream = getClass().getResourceAsStream(path);
        } else {
            try {
                fileStream = new FileInputStream(path);
            } catch (IOException ignored) {
                return false;
            }
        }

        try {
            propertiesFile.load(new InputStreamReader(fileStream, StandardCharsets.UTF_8));
            fileStream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
