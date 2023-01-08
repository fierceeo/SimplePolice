package com.voidcitymc.plugins.SimplePolice;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.config.configvalues.PayPoliceOnArrestMode;
import com.voidcitymc.plugins.SimplePolice.config.configvalues.TakeMoneyOnArrestMode;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import me.zombie_striker.qg.api.QualityArmory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public class Utility {

    public static class LoreItemStackPair {
        LoreItemStackPair (ItemStack itemStack, String displayName, List<String> lore) {
            this.itemStack = itemStack;
            this.displayName = displayName;
            this.lore = lore;
        }
        public final ItemStack itemStack;
        public final String displayName;
        public final List<String> lore;

        @Override
        public boolean equals(Object compare) {
            if (compare == this) {
                return true;
            }

            if (!(compare instanceof LoreItemStackPair)) {
                return false;
            }

            LoreItemStackPair comparePair = (LoreItemStackPair) compare;

            return itemStack.equals(comparePair.itemStack) && ((lore == null && comparePair.lore == null) || (lore != null && comparePair.lore != null && lore.equals(comparePair.lore)));
        }
    }

    public static boolean vaultEnabled() {
        return SimplePolice.vaultInstalled;
    }
    public static Economy getEconomy() {
        return SimplePolice.economy;
    }

    public static void payPoliceOnArrest(Player police, Player criminal) {
        if (ConfigValues.payPoliceOnArrest && vaultEnabled()) {
            Economy economy = getEconomy();
            if (economy != null) {
                if (ConfigValues.payPoliceOnArrestMode == PayPoliceOnArrestMode.server) {
                    economy.depositPlayer(police, ConfigValues.payPoliceOnArrestServer);
                    police.sendMessage(Messages.getMessage("MoneyEarnOnArrest", "\\$" + ConfigValues.payPoliceOnArrestServer));
                } else if (ConfigValues.payPoliceOnArrestMode == PayPoliceOnArrestMode.playerfixed) {
                    double moneyEarned = ConfigValues.payPoliceOnArrestPlayerFixed;
                    double criminalBalance = economy.getBalance(criminal);
                    if (criminalBalance < moneyEarned) {
                        moneyEarned = criminalBalance;
                    }
                    economy.depositPlayer(police, moneyEarned);
                    police.sendMessage(Messages.getMessage("MoneyEarnOnArrest", "\\$" + moneyEarned));
                } else if (ConfigValues.payPoliceOnArrestMode == PayPoliceOnArrestMode.playerpercentage) {
                    double moneyEarned = economy.getBalance(criminal) * (ConfigValues.payPoliceOnArrestPlayerPercentage / 100);
                    economy.depositPlayer(police, moneyEarned);
                    police.sendMessage(Messages.getMessage("MoneyEarnOnArrest", "\\$" + moneyEarned));
                }
            }
        }
    }

    public static void takeMoneyOnArrest(Player criminal) {
        if (ConfigValues.takeMoneyOnArrest && vaultEnabled()) {
            Economy economy = getEconomy();
            if (economy != null) {
                if (ConfigValues.takeMoneyOnArrestMode == TakeMoneyOnArrestMode.percentage) {
                    double moneyLost = economy.getBalance(criminal) * (ConfigValues.takeMoneyOnArrestPercentage / 100);
                    criminal.sendMessage(Messages.getMessage("MoneyLostOnArrest", "\\$" + moneyLost));
                    economy.withdrawPlayer(criminal, moneyLost);
                } else if (ConfigValues.takeMoneyOnArrestMode == TakeMoneyOnArrestMode.fixed) {
                    double moneyLost = ConfigValues.takeMoneyOnArrestFixed;
                    double playerBalance = economy.getBalance(criminal);
                    if (playerBalance < moneyLost) {
                        moneyLost = playerBalance;
                    }
                    criminal.sendMessage(Messages.getMessage("MoneyLostOnArrest", "\\$" + moneyLost));
                    economy.withdrawPlayer(criminal, moneyLost);
                }
            }
        }
    }

    public static boolean containsContraband(ItemStack[] inventory) {
        for (ItemStack item: inventory) {
            if (item != null && isContraband(item)) {
                return true;
            }
        }
        return false;
    }






    public static String leastCommonElement(String[] array) {
        Map<String, Integer> hashMap = new HashMap<>();

        for(int i = 0; i < array.length; i++)  {
            String key = array[i];
            if (hashMap.containsKey(key)) {
                int elementCount = hashMap.get(key);
                elementCount++;
                hashMap.put(key, elementCount);
            } else {
                hashMap.put(key, 1);
            }
        }

        int minElementCounter = array.length+1;
        String leastUsedElement = array[0];


        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            if (minElementCounter >= entry.getValue())
            {
                leastUsedElement = entry.getKey();
                minElementCounter = entry.getValue();
            }
        }
        return leastUsedElement;
    }

    public static boolean inSafeArea(Player player) {
        if (ConfigValues.safeAreaEnabled && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            ApplicableRegionSet set = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
            List<String> safeAreas = Arrays.asList(ConfigValues.safeAreas);
            for (ProtectedRegion region : set) {
                if (safeAreas.contains(region.getId())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createGuiItem(final ItemStack item, final String name, final String... lore) {
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ArrayList<String> jailList() {
        ArrayList<String> jails = new ArrayList<>();
        for (Jail.JailLocation jailLocation: DatabaseUtility.getJailLocations()) {
            jails.add(jailLocation.getJailName());
        }
        return jails;
    }

    public static boolean isPolice(String uuid) {
        return DatabaseUtility.getPoliceUUIDList().contains(uuid);
    }

    public static boolean arePoliceOnline() {
        Iterator<String> policeIterator = DatabaseUtility.getPoliceUUIDList().iterator();
        while (policeIterator.hasNext()) {
            if (Bukkit.getPlayer(UUID.fromString(policeIterator.next())) != null) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<UUID> onlinePoliceList() {
        Iterator<String> policeIterator = DatabaseUtility.getPoliceUUIDList().iterator();
        ArrayList<UUID> onlinePolice = new ArrayList<>();
        while (policeIterator.hasNext()) {
            UUID currentPlayer = UUID.fromString(policeIterator.next());
            if (Bukkit.getPlayer(currentPlayer) != null) {
                onlinePolice.add(currentPlayer);
            }
        }
        return onlinePolice;
    }

    public static void addContrabandItem(ItemStack item) {
        if (item == null || isContraband(item)) {
            return;
        }
        LoreItemStackPair itemClone = generifyItemStack(item);
        if (isQaItem(itemClone.itemStack)) {
            System.out.println("[Debug] Adding qa item "+ itemClone);
            DatabaseUtility.addQAItem(QualityArmory.getCustomItem(itemClone.itemStack));
        } else {
            System.out.println("[Debug] Adding item "+ itemClone);
            DatabaseUtility.addItem(itemClone);
        }
    }

    public static void removeContrabandItem(ItemStack item) {
        if (item == null || !isContraband(item)) {
            return;
        }
        LoreItemStackPair itemClone = generifyItemStack(item);
        if (isQaItem(itemClone.itemStack)) {
            DatabaseUtility.addQAItem(QualityArmory.getCustomItem(itemClone.itemStack));
        } else {
            DatabaseUtility.addItem(itemClone);
        }
    }

    public static boolean isQaItem(ItemStack item) {
        return (SimplePolice.qaInstalled && (QualityArmory.isCustomItem(item)));
    }

    public static CustomBaseObject generifyQACustomBaseObject(CustomBaseObject item) {
        item.setPrice(0);
        item.setIngredients(null);
        item.setCraftingReturn(1);
        item.setMaxItemStack(1);
        item.setSoundOnHit(null);
        item.setSoundOnEquip(null);
        item.setCustomLore(null);
        return item;
    }

    public static LoreItemStackPair generifyItemStack(ItemStack item) {
        if (item == null) {
            return null;
        }
        ItemStack itemClone = item.clone();
        itemClone.setAmount(1);

        if (isQaItem(item)) {
            new LoreItemStackPair(itemClone, null, null);
        }

        List<String> lore = null;
        String displayName = null;
        if (itemClone.getItemMeta().hasLore()) {
            lore = itemClone.getItemMeta().getLore();
            displayName = itemClone.getItemMeta().getDisplayName();
        }

        itemClone.setItemMeta(null);

        return new LoreItemStackPair(itemClone, displayName, lore);
    }

    public static boolean isContraband(ItemStack item) {
        if (isQaItem(item) && DatabaseUtility.getContrabandQAItems().contains(generifyQACustomBaseObject(QualityArmory.getCustomItem(generifyItemStack(item).itemStack)))) {
            return true;
        } else if (DatabaseUtility.getContrabandItems().contains(generifyItemStack(item))) {
            return true;
        }
        return false;
    }

    //todo: cleanup method
    public static boolean isLocationSafe(Location loc1) {
        Location loc2 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + 1, loc1.getZ());
        //check if the location is safe to teleport to (air)
        return (loc1.getBlock().getType().equals(Material.AIR) && loc2.getBlock().getType().equals(Material.AIR));
    }

    //todo: cleanup method
    public static Location policeTp(Player player, int MaxValTp) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();

        int pX = playerLocation.getBlockX();
        int pY = playerLocation.getBlockY();
        int pZ = playerLocation.getBlockZ();

        //generate a random number from 0 to the farthest /police tp value in config (MaxValTp)
        pX += Math.round((Math.random() * 2 * MaxValTp) - MaxValTp);
        pY += Math.round((Math.random() * 2 * MaxValTp) - MaxValTp);

        Location checkLocation = new Location(world, pX, pY, pZ);

        //keeps increasing y cord until location is safe
        while (!isLocationSafe(checkLocation)) {
            pY += 1;
            if ((pY + 1) >= world.getMaxHeight()) {
                //unable to find location, escape by returning player's position
                checkLocation = playerLocation;
                break;
            }
            
            checkLocation = new Location(player.getWorld(), pX, pY, pZ);
        }

        return checkLocation;
    }

    public static String timeUnit(int seconds) {
        String timeUnit = "";

        if (seconds == 1) {
            timeUnit = 1 + " " + Messages.getMessage("TimeUnitForTimeLeftEqual1");
        } else if (seconds < 60) {
            timeUnit = seconds + " " + Messages.getMessage("TimeUnitForTimeLeftUnder60");
        } else if (seconds == 60) {
            timeUnit = 1 + " " + Messages.getMessage("TimeUnitForTimeLeftEqual60");
        } else {
            timeUnit = (seconds / 60) + " " + Messages.getMessage("TimeUnitForTimeLeftOver60");
        }
        return timeUnit;
    }
}
