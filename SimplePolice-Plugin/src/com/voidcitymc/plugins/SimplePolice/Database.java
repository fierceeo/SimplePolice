package com.voidcitymc.plugins.SimplePolice;

import org.bukkit.inventory.ItemStack;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Database {
    public static DB simplePoliceDB;

    public static ArrayList<ItemStack> contrabandItems;
    public static ArrayList<String> contrabandQAItems;
    public static ArrayList<String> policeUUIDList;
    public static HashMap<String, com.voidcitymc.plugins.SimplePolice.config.configvalues.Location> jailLocations;

    protected static void iniDatabase() {
        (new File("."+ File.separator+"plugins"+File.separator+"SimplePolice"+File.separator+"Database"+File.separator)).mkdirs();
        simplePoliceDB = DBMaker.fileDB("./plugins/SimplePolice/Database/SimplePolice.db").transactionEnable().make();
    }

    protected static void loadDataIntoMemory() {
        //load contraband items list
        contrabandItems = new ArrayList<>();
        NavigableSet<byte[]> contrabandItemsDB = Database.simplePoliceDB.treeSet("contrabandItems").serializer(Serializer.BYTE_ARRAY).createOrOpen();
        Iterator<byte[]> contrabandItemsDBIterator = contrabandItemsDB.iterator();
        while (contrabandItemsDBIterator.hasNext()) {
            byte[] serializedItem = contrabandItemsDBIterator.next();
            ItemStack item = null;
            try {
                item = Worker.deserializeItemStack(serializedItem);
            } catch (IOException | ClassNotFoundException ignored) {
            }
            if (item == null) {
                contrabandItemsDB.remove(serializedItem);
            } else {
                contrabandItems.add(item);
            }
        }

        //load contraband QA items list
        contrabandQAItems = new ArrayList<>();
        NavigableSet<String> contrabandQAItemsDB = Database.simplePoliceDB.treeSet("contrabandQAItems").serializer(Serializer.STRING).createOrOpen();
        contrabandQAItems.addAll(contrabandQAItemsDB);

        //load police list
        policeUUIDList = new ArrayList<>();
        NavigableSet<String> policeUUIDListDB = Database.simplePoliceDB.treeSet("policeUUIDList").serializer(Serializer.STRING).createOrOpen();
        policeUUIDList.addAll(policeUUIDListDB);

        //load jail locations list
        jailLocations = new HashMap<>();
        BTreeMap<String, String> jailLocationsDB = Database.simplePoliceDB.treeMap("jailLocations").keySerializer(Serializer.STRING).valueSerializer(Serializer.STRING).createOrOpen();
        Iterator<Map.Entry<String, String>> jailLocationsDBIterator = jailLocationsDB.entryIterator();
        while (jailLocationsDBIterator.hasNext()) {
            Map.Entry<String, String> currentEntry = jailLocationsDBIterator.next();
            jailLocations.put(currentEntry.getKey(), new com.voidcitymc.plugins.SimplePolice.config.configvalues.Location(currentEntry.getValue()));
        }
    }

    protected static void close() {
        simplePoliceDB.close();
    }
}
