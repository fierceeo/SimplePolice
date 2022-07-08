package com.voidcitymc.plugins.SimplePolice;

import com.google.gson.reflect.TypeToken;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

public class DatabaseUtility {

    static class LoreSeralizedItemStackPair {
        LoreSeralizedItemStackPair (Map<String, Object> itemStack, String displayName, List<String> lore) {
            this.itemStack = itemStack;
            this.displayName = displayName;
            this.lore = lore;
        }
        public final Map<String, Object> itemStack;
        public final String displayName;
        public final List<String> lore;
    }

    private static Database<LoreSeralizedItemStackPair> contrabandItemsDB;

    private static Database<CustomBaseObject> contrabandQAItemsDB;

    private static Database<String> policeUUIDListDB;

    private static Database<Jail.JailLocation> jailLocationsDB;


    protected static void initDatabase() {
        (new File(SimplePolice.getPluginFolderPath()+File.separator+"Database")).mkdirs();
        contrabandItemsDB = new Database<>("contrabandItems", new TypeToken<ArrayList<LoreSeralizedItemStackPair>>(){});
        contrabandItemsDB.load();
        if (SimplePolice.qaInstalled) {
            contrabandQAItemsDB = new Database<>("contrabandQaItems", new TypeToken<ArrayList<CustomBaseObject>>() {});
            contrabandQAItemsDB.load();
        }
        policeUUIDListDB = new Database<>("policeList", new TypeToken<ArrayList<String>>() {});
        policeUUIDListDB.load();
        jailLocationsDB = new Database<>("jailLocations", new TypeToken<ArrayList<Jail.JailLocation>>() {});
        jailLocationsDB.load();
    }

    public static ArrayList<CustomBaseObject> getContrabandQAItems() {
        return contrabandQAItemsDB.getData();
    }

    public static ArrayList<Utility.LoreItemStackPair> getContrabandItems() {
        ArrayList<Utility.LoreItemStackPair> contrabandItems = new ArrayList<>();
        for (LoreSeralizedItemStackPair item: contrabandItemsDB.getData()) {
            contrabandItems.add(new Utility.LoreItemStackPair(ItemStack.deserialize(item.itemStack), item.displayName, item.lore));
        }
        return contrabandItems;
    }

    public static ArrayList<Jail.JailLocation> getJailLocations() {
        return jailLocationsDB.getData();
    }

    public static ArrayList<String> getPoliceUUIDList() {
        return policeUUIDListDB.getData();
    }

    protected static void addQAItem(CustomBaseObject baseItem) {
        if (contrabandQAItemsDB == null) {
            return;
        }
        if (baseItem == null) {
            return;
        }
        CustomBaseObject genericBaseItem = Utility.generifyQACustomBaseObject(baseItem);
        contrabandQAItemsDB.add(genericBaseItem);
        contrabandQAItemsDB.save();
    }

    protected static void removeQAItem(CustomBaseObject baseItem) {
        if (contrabandQAItemsDB == null) {
            return;
        }
        if (baseItem == null) {
            return;
        }
        CustomBaseObject genericBaseItem = Utility.generifyQACustomBaseObject(baseItem);
        contrabandQAItemsDB.remove(genericBaseItem);
        contrabandQAItemsDB.save();
    }

    protected static void addItem(Utility.LoreItemStackPair item) {
        if (item == null) {
            return;
        }

        contrabandItemsDB.add(new LoreSeralizedItemStackPair(item.itemStack.serialize(), item.displayName, item.lore));
        contrabandItemsDB.save();
    }

    protected static void removeItem(Utility.LoreItemStackPair item) {
        contrabandItemsDB.remove(new LoreSeralizedItemStackPair(item.itemStack.serialize(), item.displayName, item.lore));
        contrabandItemsDB.save();
    }

    public static void addPolice(String uuid) {
        if (!policeUUIDListDB.getData().contains(uuid)) {
            policeUUIDListDB.add(uuid);
            policeUUIDListDB.save();
        }
    }

    public static void removePolice(String uuid) {
        policeUUIDListDB.remove(uuid);
        policeUUIDListDB.save();
    }

    public static void addJail(String jailName, Location location) {
        Jail.JailLocation jailLocation = new Jail.JailLocation(jailName, location);
        if (!jailLocationsDB.getData().contains(jailLocation)) {
            jailLocationsDB.add(jailLocation);
            jailLocationsDB.save();
        }
    }

    public static void removeJail(String jailName) {
        for (Jail.JailLocation jailLocation: jailLocationsDB.getData()) {
            if (jailLocation.getJailName().equals(jailName)) {
                jailLocationsDB.remove(jailLocation);
            }
        }
        jailLocationsDB.save();
    }

    protected static void save() {
        contrabandItemsDB.save();
        if (contrabandQAItemsDB != null) {
            contrabandQAItemsDB.save();
        }
        policeUUIDListDB.save();
        jailLocationsDB.save();
    }
}
