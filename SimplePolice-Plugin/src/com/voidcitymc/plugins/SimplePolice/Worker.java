package com.voidcitymc.plugins.SimplePolice;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.config.configvalues.PayPoliceOnArrestMode;
import com.voidcitymc.plugins.SimplePolice.config.configvalues.TakeMoneyOnArrestMode;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import me.zombie_striker.qg.api.QualityArmory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mapdb.BTreeMap;
import org.mapdb.Serializer;

import java.io.*;
import java.util.*;


public class Worker {

    public static boolean vaultEnabled() {
        return SimplePolice.vaultEnabled;
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

    public static void addJail(String jailName, Location location) {
        BTreeMap<String, String> jailLocationsDB = Database.simplePoliceDB.treeMap("jailLocations").keySerializer(Serializer.STRING).valueSerializer(Serializer.STRING).createOrOpen();
        com.voidcitymc.plugins.SimplePolice.config.configvalues.Location storageLocation = new com.voidcitymc.plugins.SimplePolice.config.configvalues.Location(location);
        jailLocationsDB.put(jailName.toLowerCase(), storageLocation.toStorageString());
        Database.simplePoliceDB.commit();
        Database.jailLocations.put(jailName.toLowerCase(), storageLocation);
    }

    public static void removeJail(String jailName) {
        BTreeMap<String, String> jailLocationsDB = Database.simplePoliceDB.treeMap("jailLocations").keySerializer(Serializer.STRING).valueSerializer(Serializer.STRING).createOrOpen();
        jailLocationsDB.remove(jailName.toLowerCase());
        Database.simplePoliceDB.commit();
        Database.jailLocations.remove(jailName.toLowerCase());
    }

    public static ArrayList<String> jailList() {
        return new ArrayList<>(Database.jailLocations.keySet());
    }

    //todo: add gun contraband item
    public static void addPolice(String uuid) {
        if (!isPolice(uuid)) {
            NavigableSet<String> policeUUIDListDB = Database.simplePoliceDB.treeSet("policeUUIDList").serializer(Serializer.STRING).createOrOpen();
            policeUUIDListDB.add(uuid);
            Database.simplePoliceDB.commit();
            Database.policeUUIDList.add(uuid);
        }
    }

    public static void removePolice(String uuid) {
        if (isPolice(uuid)) {
            NavigableSet<String> policeUUIDListDB = Database.simplePoliceDB.treeSet("policeUUIDList").serializer(Serializer.STRING).createOrOpen();
            policeUUIDListDB.remove(uuid);
            Database.simplePoliceDB.commit();
            Database.policeUUIDList.remove(uuid);
        }
    }

    public static boolean isPolice(String uuid) {
        return Database.policeUUIDList.contains(uuid);
    }

    public static boolean arePoliceOnline() {
        Iterator<String> policeIterator = Database.policeUUIDList.iterator();
        while (policeIterator.hasNext()) {
            if (Bukkit.getPlayer(UUID.fromString(policeIterator.next())) != null) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<UUID> onlinePoliceList() {
        Iterator<String> policeIterator = Database.policeUUIDList.iterator();
        ArrayList<UUID> onlinePolice = new ArrayList<>();
        while (policeIterator.hasNext()) {
            UUID currentPlayer = UUID.fromString(policeIterator.next());
            if (Bukkit.getPlayer(currentPlayer) != null) {
                onlinePolice.add(currentPlayer);
            }
        }
        return onlinePolice;
    }

    public static byte[] serializeItemStack(ItemStack itemStack) throws IOException {
        if (itemStack != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteArrayOutputStream);
            objStream.writeObject(itemStack);
            return byteArrayOutputStream.toByteArray();
        } else {
            return null;
        }
    }

    public static ItemStack deserializeItemStack(byte[] serializedItemStack) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedItemStack);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object objectItemStack = objectInputStream.readObject();
        if (objectItemStack instanceof ItemStack) {
            return (ItemStack) objectItemStack;
        } else {
            return null;
        }
    }

    public static void addContrabandItem(ItemStack itemStack) {
        if (itemStack != null) {
            ItemStack itemStackCorrectedAmount = itemStack.clone();
            itemStackCorrectedAmount.setAmount(1);
            if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null && QualityArmory.isCustomItem(itemStackCorrectedAmount)) {
                NavigableSet<String> contrabandQAItemsDB = Database.simplePoliceDB.treeSet("contrabandQAItems").serializer(Serializer.STRING).createOrOpen();
                String qaItemName = QualityArmory.getCustomItem(itemStackCorrectedAmount).getName();
                contrabandQAItemsDB.add(qaItemName);
                Database.contrabandQAItems.add(qaItemName);
            } else {
                NavigableSet<byte[]> contrabandItemsDB = Database.simplePoliceDB.treeSet("contrabandItems").serializer(Serializer.BYTE_ARRAY).createOrOpen();
                byte[] serializedItemStack = null;
                try {
                    serializedItemStack = serializeItemStack(itemStackCorrectedAmount);
                } catch (IOException ignored) {
                }
                if (serializedItemStack != null) {
                    contrabandItemsDB.add(serializedItemStack);
                }
                Database.contrabandItems.add(itemStackCorrectedAmount);
            }
            Database.simplePoliceDB.commit();
        }
    }

    public static void removeContrabandItem(ItemStack itemStack) {
        if (itemStack != null) {
            ItemStack itemStackCorrectedAmount = itemStack.clone();
            itemStackCorrectedAmount.setAmount(1);
            if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null && QualityArmory.isCustomItem(itemStackCorrectedAmount)) {
                NavigableSet<String> contrabandQAItemsDB = Database.simplePoliceDB.treeSet("contrabandQAItems").serializer(Serializer.STRING).createOrOpen();
                String qaItemName = QualityArmory.getCustomItem(itemStackCorrectedAmount).getName();
                contrabandQAItemsDB.remove(qaItemName);
                Database.contrabandQAItems.remove(qaItemName);
            } else {
                NavigableSet<byte[]> contrabandItemsDB = Database.simplePoliceDB.treeSet("contrabandItems").serializer(Serializer.BYTE_ARRAY).createOrOpen();
                byte[] serializedItemStack = null;
                try {
                    serializedItemStack = serializeItemStack(itemStackCorrectedAmount);
                } catch (IOException ignored) {
                }
                if (serializedItemStack != null) {
                    contrabandItemsDB.remove(serializedItemStack);
                }
                Database.contrabandItems.remove(itemStackCorrectedAmount);
            }
            Database.simplePoliceDB.commit();
        }
    }

    public static boolean isContraband(ItemStack itemStack) {
        if (itemStack != null) {
            ItemStack itemStackCorrectedAmount = itemStack.clone();
            itemStackCorrectedAmount.setAmount(1);
            if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null && QualityArmory.isCustomItem(itemStackCorrectedAmount)) {
                if (ConfigValues.markAllGunsAsContraband) {
                    return true;
                } else {
                    return Database.contrabandQAItems.contains(QualityArmory.getCustomItem(itemStackCorrectedAmount).getName());
                }
            }
            return Database.contrabandItems.contains(itemStackCorrectedAmount);
        } else {
            return false;
        }
    }

    //todo: cleanup method
    public static boolean isLocationSafe(Location loc1) {
        //check if the location is safe to teleport to (air)
        if (loc1.getBlock().getType().equals(Material.AIR)) {
            //we know that the inital location is safe, so we need to check one block up
            //location one block up
            Location loc2 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + 1, loc1.getZ());
            //2nd block is safe too
            return loc2.getBlock().getType().equals(Material.AIR);
            //loc2 didn't check out
        }
        //because that method didn't pass, we know that the location isn't safe
        return false;
    }

    //todo: cleanup method
    public static Location policeTp(Player player, int MaxValTp) {
        Location LocP = player.getLocation();
        //players x,y,z
        int pX = LocP.getBlockX();
        int pY = LocP.getBlockY();
        int pZ = LocP.getBlockZ();

        //generate a random number from 0 to the farthest /police tp value in config (MaxValTp)
        double drandom1 = Math.random() * MaxValTp;
        double drandom2 = Math.random() * MaxValTp;
        int random1 = (int) Math.round(drandom1);
        int random2 = (int) Math.round(drandom2);

        //should number be negative or positave

        //number from 0 to 1
        int posOrNeg = (int) Math.round(Math.random());
        if (posOrNeg == 1) {
            random1 = random1 - (2 * random1);
        }

        posOrNeg = (int) Math.round(Math.random());
        if (posOrNeg == 1) {
            random2 = random2 - (2 * random2);
        }


        int nX = pX + random1;
        int nY = pY;
        int nZ = pZ + random2;

        Location returnLocYOneDown = new Location(player.getWorld(), nX, nY - 1, nZ);

        while (returnLocYOneDown.getBlock().getType().equals(Material.AIR)) {
            nY = nY - 1;
            returnLocYOneDown = new Location(player.getWorld(), nX, nY - 1, nZ);
        }

        Location returnLoc = returnLocYOneDown;


        //keeps increasing y cord until location is safe
        while (!isLocationSafe(returnLoc)) {
            nY = nY + 1;
            returnLoc = new Location(player.getWorld(), nX, nY, nZ);
        }

        return returnLoc;


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
