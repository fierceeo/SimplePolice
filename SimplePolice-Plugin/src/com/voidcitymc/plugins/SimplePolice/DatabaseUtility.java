package com.voidcitymc.plugins.SimplePolice;

import com.google.gson.reflect.TypeToken;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import me.zombie_striker.customitemmanager.MaterialStorage;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class DatabaseUtility {

    private static ArrayList<ItemStack> contrabandItems = new ArrayList<>();
    private static Database<Map<String, Object>> contrabandItemsDB;

    private static ArrayList<CustomBaseObject> contrabandQAItems = new ArrayList<>();
    private static Database<CustomBaseObject> contrabandQAItemsDB;

    private static ArrayList<String> policeUUIDList = new ArrayList<>();
    private static Database<String> policeUUIDListDB;

    private static ArrayList<Jail.JailLocation> jailLocations = new ArrayList<>();
    private static Database<Jail.JailLocation> jailLocationsDB;


    protected static void iniDatabase() {
        (new File(SimplePolice.getPluginFolderPath()+File.separator+"Database")).mkdirs();
        contrabandItemsDB = new Database<>("contrabandItems", new TypeToken<ArrayList<Map<String, Object>>>(){});
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
        return contrabandQAItems;
    }

    public static ArrayList<ItemStack> getContrabandItems() {
        return contrabandItems;
    }

    public static ArrayList<Jail.JailLocation> getJailLocations() {
        return jailLocations;
    }

    public static ArrayList<String> getPoliceUUIDList() {
        return policeUUIDList;
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
        contrabandQAItems.add(genericBaseItem);
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
        contrabandQAItems.remove(genericBaseItem);
        contrabandQAItemsDB.save();
    }

    protected static void addItem(ItemStack item) {
        if (item == null) {
            return;
        }
        ItemStack genericItem = Utility.generifyItemStack(item);
        contrabandItemsDB.add(genericItem.serialize());
        contrabandItems.add(genericItem);
        contrabandItemsDB.save();
    }

    protected static void removeItem(ItemStack item) {
        contrabandItemsDB.remove(item.serialize());
        contrabandItems.remove(item);
        contrabandItemsDB.save();
    }

    public static void addPolice(String uuid) {
        if (!policeUUIDList.contains(uuid)) {
            policeUUIDListDB.add(uuid);
            policeUUIDList.add(uuid);
            policeUUIDListDB.save();
        }
    }

    public static void removePolice(String uuid) {
        policeUUIDListDB.remove(uuid);
        policeUUIDList.remove(uuid);
        policeUUIDListDB.save();
    }

    public static void addJail(String jailName, Location location) {
        Jail.JailLocation jailLocation = new Jail.JailLocation(jailName, location);
        jailLocationsDB.add(jailLocation);
        jailLocations.add(jailLocation);
        jailLocationsDB.save();
    }

    public static void removeJail(String jailName) {
        for (Jail.JailLocation jailLocation: jailLocationsDB.getData()) {
            if (jailLocation.getJailName().equals(jailName)) {
                jailLocations.remove(jailLocation);
                jailLocationsDB.remove(jailLocation);
            }
        }
        jailLocationsDB.save();
    }

    protected static void loadDatabase() {
        //contraband items
        for (Map<String, Object> rawItemStack: contrabandItemsDB.getData()) {
            contrabandItems.add(ItemStack.deserialize(rawItemStack));
        }

        if (contrabandQAItemsDB != null) {
            //qa contraband items
            contrabandQAItems.addAll(contrabandQAItemsDB.getData());
        }

        //police list
        policeUUIDList.addAll(policeUUIDListDB.getData());

        //jail locations list
        jailLocations.addAll(jailLocationsDB.getData());

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
