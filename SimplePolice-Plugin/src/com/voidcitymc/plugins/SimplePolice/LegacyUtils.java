package com.voidcitymc.plugins.SimplePolice;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.InvocationTargetException;

public class LegacyUtils {

    public static ItemStack getStainedClay(DyeColor dyeColor) {

        //post 1.13
        if (enumExists(Material.class, "RED_TERRACOTTA")) {
            switch (dyeColor) {
                case RED:
                    return new ItemStack(Material.RED_TERRACOTTA,1);
                case ORANGE:
                    return new ItemStack(Material.ORANGE_TERRACOTTA,1);
                case YELLOW:
                    return new ItemStack(Material.YELLOW_TERRACOTTA,1);
                case LIME:
                    return new ItemStack(Material.LIME_TERRACOTTA,1);
                case LIGHT_BLUE:
                    return new ItemStack(Material.LIGHT_BLUE_TERRACOTTA,1);
            }
        }

        //pre 1.13
        try {
            byte woolData = (byte) DyeColor.class.getDeclaredMethod("getWoolData").invoke(dyeColor);
            return ItemStack.class.getConstructor(Material.class, Integer.TYPE, Short.TYPE).newInstance(Enum.valueOf(Material.class, "STAINED_CLAY"), 1, (short)woolData);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
        //return null;
    }

    public static ItemStack getItemInMainHand(PlayerInventory playerInventory) {
        //post 1.9F
        if (methodExists(PlayerInventory.class, "getItemInMainHand")) {
            return playerInventory.getItemInMainHand();
        }

        //pre 1.9
        try {
            return (ItemStack) PlayerInventory.class.getDeclaredMethod("getItemInHand").invoke(playerInventory);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }


    }

    public static boolean methodExists(Class className, String methodName) {
        try {
            className.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    public static boolean enumExists(Class className, String enumKey) {
        try {
            Enum.valueOf(className, enumKey);
        } catch (Exception e) {
            return false;
        }
        return true;
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
