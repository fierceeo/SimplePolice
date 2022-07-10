package com.voidcitymc.plugins.SimplePolice.api;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;



public interface Utility {

    void payPoliceOnArrest(Player police, Player criminal);

    void takeMoneyOnArrest(Player criminal);

    String leastCommonElement(String[] array);

    boolean inSafeArea(Player player);

    ItemStack createGuiItem(final Material material, final String name, final String... lore);

    void addJail(String jailName, Location location);

    void removeJail(String jailName);

    Location getJailLocation(String jailName);

    ArrayList<String> jailList();

    ArrayList<UUID> jailedPlayers();

    boolean isJailed(UUID player);

    String getPlayerCurrentJail(UUID player);

    double sentenceLength(UUID player);

    double sentenceLengthLeft(UUID player);

    void addPolice(String uuid);

    void removePolice(String uuid);

    boolean isPolice(String uuid);

    boolean arePoliceOnline();

    ArrayList<UUID> onlinePoliceList();

    void addContrabandItem(ItemStack itemStack);

    void removeContrabandItem(ItemStack itemStack);

    boolean isContraband(ItemStack itemStack);

    boolean isLocationSafe(Location location);

    Location policeTp(Player player, int farthestTpDistance);

}
