package com.voidcitymc.plugins.SimplePolice;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class LegacyUtils {
    public static ItemStack getLegacyStainedClay(DyeColor dyeColor) {
        try {
            short woolData = (short) DyeColor.class.getDeclaredMethod("getWoolData").invoke(dyeColor);
            return ItemStack.class.getConstructor(Material.class, Integer.TYPE, Short.TYPE).newInstance(Enum.valueOf(Material.class, "STAINED_CLAY"), 1, woolData);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }
}
