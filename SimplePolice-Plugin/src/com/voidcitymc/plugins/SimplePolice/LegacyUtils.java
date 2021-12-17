package com.voidcitymc.plugins.SimplePolice;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.InvocationTargetException;

public class LegacyUtils {

    public static Material getStainedClay(DyeColor dyeColor) {
        //pre 1.13
        if (!isDeprecated(DyeColor.class, "getWoolData")) {
            try {
                short woolData = (short) DyeColor.class.getDeclaredMethod("getWoolData").invoke(dyeColor);
                return ItemStack.class.getConstructor(Material.class, Integer.TYPE, Short.TYPE).newInstance(Enum.valueOf(Material.class, "STAINED_CLAY"), 1, woolData).getType();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        }

        //post 1.13
        switch (dyeColor) {
            case RED:
                return Material.RED_TERRACOTTA;
            case ORANGE:
                return Material.ORANGE_TERRACOTTA;
            case YELLOW:
                return Material.YELLOW_TERRACOTTA;
            case LIME:
                return Material.LIME_TERRACOTTA;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_TERRACOTTA;
        }

        return null;
    }

    public static ItemStack getItemInMainHand(PlayerInventory playerInventory) {
        //pre 1.9
        if (!isDeprecated(PlayerInventory.class, "getItemInHand")) {
            try {
                return (ItemStack) PlayerInventory.class.getDeclaredMethod("getItemInHand").invoke(playerInventory);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        }

        //post 1.9F
        return playerInventory.getItemInMainHand();
    }

    public static boolean isDeprecated(Class className, String methodName) {
        try {
            if (className.getDeclaredMethod(methodName).getAnnotation(Deprecated.class) != null) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            return true;
        }
        return false;
    }
}
