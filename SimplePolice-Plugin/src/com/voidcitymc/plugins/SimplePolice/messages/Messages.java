package com.voidcitymc.plugins.SimplePolice.messages;

import com.voidcitymc.plugins.SimplePolice.config.Config;
import net.md_5.bungee.api.ChatColor;

public class Messages {

    private static Config messagesConfig;
    public static void initialize (Config messagesConfig) {
        Messages.messagesConfig = messagesConfig;
    }

    public static String getMessage(String property) {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getProperty(property));
    }
    public static String getMessage(String property, String... textToReplace) {
        String message = messagesConfig.getProperty(property);
        for (int i = 0; i < textToReplace.length; i++) {
            message = message.replaceFirst("&0", textToReplace[i]);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
