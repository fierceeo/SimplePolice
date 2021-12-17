package com.voidcitymc.plugins.SimplePolice;

import com.google.gson.reflect.TypeToken;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

public class DatabaseUtility {

    private static Database<Map<String, Object>> contrabandItemsDB;

    private static Database<CustomBaseObject> contrabandQAItemsDB;

    private static Database<String> policeUUIDListDB;

    private static Database<Jail.JailLocation> jailLocationsDB;


    protected static void initDatabase() {
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
        return contrabandQAItemsDB.getData();
    }

    public static ArrayList<ItemStack> getContrabandItems() {
        ArrayList<ItemStack> contrabandItems = new ArrayList<>();
        for (Map<String, Object> item: contrabandItemsDB.getData()) {
            contrabandItems.add(ItemStack.deserialize(item));
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

    protected static void addItem(ItemStack item) {
        if (item == null) {
            return;
        }
        ItemStack genericItem = Utility.generifyItemStack(item);
        contrabandItemsDB.add(genericItem.serialize());
        contrabandItemsDB.save();
    }

    protected static void removeItem(ItemStack item) {
        contrabandItemsDB.remove(item.serialize());
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
        if (!jailLocationsDB.getData().contains(jailName)) {
            Jail.JailLocation jailLocation = new Jail.JailLocation(jailName, location);
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
