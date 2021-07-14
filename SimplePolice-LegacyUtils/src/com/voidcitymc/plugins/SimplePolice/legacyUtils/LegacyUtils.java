package com.voidcitymc.plugins.SimplePolice.legacyUtils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LegacyUtils {
    public static ItemStack getLegacyStainedClay(DyeColor dyeColor) {
        return new ItemStack(Material.STAINED_CLAY, 1, dyeColor.getWoolData());
    }
}
