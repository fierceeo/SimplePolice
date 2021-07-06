package com.voidcitymc.plugins.SimplePolice.messages;

import com.voidcitymc.plugins.SimplePolice.config.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Messages {

    private static Config messagesConfig;
    public static void initialize (Config messagesConfig) {
        Messages.messagesConfig = messagesConfig;
    }

    public static String getMessage(String property) {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getConfigProperty(property));
    }
    public static String getMessage(String property, String... textToReplace) {
        String message = messagesConfig.getConfigProperty(property);
        for (int i = 0; i < textToReplace.length; i++) {
            message = message.replaceFirst("&0", textToReplace[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
