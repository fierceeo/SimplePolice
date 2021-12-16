package com.voidcitymc.plugins.SimplePolice;

import me.zombie_striker.customitemmanager.CustomBaseObject;
import me.zombie_striker.customitemmanager.MaterialStorage;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class DatabaseUtility {

    private static ArrayList<ItemStack> contrabandItems = new ArrayList<>();
    private static Database contrabandItemsDB;

    private static ArrayList<CustomBaseObject> contrabandQAItems = new ArrayList<>();
    private static Database contrabandQAItemsDB;

    private static ArrayList<String> policeUUIDList = new ArrayList<>();
    private static Database policeUUIDListDB;

    private static ArrayList<JailLocation> jailLocations = new ArrayList<>();
    private static Database jailLocationsDB;


    protected static void iniDatabase() {
        (new File(SimplePolice.getPluginFolderPath()+File.separator+"Database")).mkdirs();
        contrabandItemsDB = new Database("contrabandItems");
        contrabandItemsDB.load();
        if (SimplePolice.qaInstalled) {
            contrabandQAItemsDB = new Database("contrabandQaItems");
            contrabandQAItemsDB.load();
        }
        policeUUIDListDB = new Database("policeList");
        policeUUIDListDB.load();
        jailLocationsDB = new Database("jailLocations");
        jailLocationsDB.load();
    }

    public static ArrayList<CustomBaseObject> getContrabandQAItems() {
        return contrabandQAItems;
    }

    public static ArrayList<ItemStack> getContrabandItems() {
        return contrabandItems;
    }

    public static ArrayList<JailLocation> getJailLocations() {
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
        contrabandQAItemsDB.add(new CustomBaseObjectWrapper(genericBaseItem));
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
        contrabandQAItemsDB.remove(new CustomBaseObjectWrapper(genericBaseItem));
        contrabandQAItems.remove(genericBaseItem);
        contrabandQAItemsDB.save();
    }

    protected static void addItem(ItemStack item) {
        if (item == null) {
            return;
        }
        ItemStack genericItem = Utility.generifyItemStack(item);
        contrabandItemsDB.add(new ItemStackWrapper(genericItem));
        contrabandItems.add(genericItem);
        contrabandItemsDB.save();
    }

    protected static void removeItem(ItemStack item) {
        contrabandItemsDB.remove(new ItemStackWrapper(item));
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
        JailLocation jailLocation = new JailLocation(jailName, location);
        jailLocationsDB.add(jailLocation);
        jailLocations.add(jailLocation);
        jailLocationsDB.save();
    }

    public static void removeJail(String jailName) {
        for (Object object: jailLocationsDB.getData()) {
            if (object instanceof JailLocation && ((JailLocation) object).getJailName().equals(jailName)) {
                jailLocations.remove(object);
                jailLocationsDB.remove(object);
            }
        }
        jailLocationsDB.save();
    }

    protected static void loadDatabase() {
        //contraband items
        for (Object objectItem: contrabandItemsDB.getData()) {
            if (objectItem instanceof ItemStackWrapper) {
                contrabandItems.add(((ItemStackWrapper) objectItem).getItemStack());
            }
        }

        if (contrabandQAItemsDB != null) {
            //qa contraband items
            for (Object objectItem : contrabandQAItemsDB.getData()) {
                if (objectItem instanceof CustomBaseObjectWrapper) {
                    contrabandQAItems.add(((CustomBaseObjectWrapper) objectItem).getCustomBaseObject());
                }
            }
        }

        //police list
        for (Object objectString: policeUUIDListDB.getData()) {
            if (objectString instanceof String) {
                policeUUIDList.add((String) objectString);
            }
        }

        //jail locations list
        for (Object locationObject: jailLocationsDB.getData()) {
            if (locationObject instanceof JailLocation) {
                jailLocations.add((JailLocation) locationObject);
            }
        }

    }

    protected static void save() {
        contrabandItemsDB.save();
        if (contrabandQAItemsDB != null) {
            contrabandQAItemsDB.save();
        }
        policeUUIDListDB.save();
        jailLocationsDB.save();
    }

    public static class JailLocation implements Serializable {
        private String jailName;
        private Map<String, Object> location;

        public JailLocation(String jailName, Location location) {
            this.jailName = jailName;
            this.location = location.serialize();
        }

        public String getJailName() {
            return jailName;
        }

        public Location getLocation() {
            return Location.deserialize(location);
        }
    }

    private static class ItemStackWrapper {
        private Map<String, Object> itemstack;

        public ItemStackWrapper(ItemStack itemStack) {
            this.itemstack = itemStack.serialize();
        }

        public ItemStack getItemStack() {
            return ItemStack.deserialize(itemstack);
        }
    }

    public static class CustomBaseObjectWrapper {
        private String name;
        private MaterialStorage storage;
        private String displayname;
        private List<String> lore;
        private boolean hasAimAnimations;

        public CustomBaseObjectWrapper(CustomBaseObject customBaseObject) {
            this.name = customBaseObject.getName();
            this.storage = customBaseObject.getItemData();
            this.displayname = customBaseObject.getDisplayName();

            try {
                Field loreField = CustomBaseObject.class.getField("lore");
                loreField.setAccessible(true);
                this.lore = (List<String>) loreField.get(customBaseObject);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
                this.lore = customBaseObject.getCustomLore();
            }

            //access has aim animations which is a protected field
            try {
                Field hasAimAnimationsField = CustomBaseObject.class.getField("hasAimAnimations");
                hasAimAnimationsField.setAccessible(true);
                this.hasAimAnimations = (boolean) hasAimAnimationsField.get(customBaseObject);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {}
        }

        public CustomBaseObject getCustomBaseObject() {
            return new CustomBaseObject(name, storage, displayname, lore, hasAimAnimations);
        }
    }
}
