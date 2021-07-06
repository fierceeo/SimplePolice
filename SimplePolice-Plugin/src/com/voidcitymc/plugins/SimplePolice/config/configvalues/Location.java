package com.voidcitymc.plugins.SimplePolice.config.configvalues;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Location {

    public int x;
    public int y;
    public int z;
    public String world;

    public Location(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Location(String storageString) {
        String[] configStringArray = storageString.split(",");
        if (configStringArray.length != 4) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.world = "world";
        }
        x = Integer.parseInt(configStringArray[0]);
        y = Integer.parseInt(configStringArray[1]);
        z = Integer.parseInt(configStringArray[2]);
        world = configStringArray[3];
    }

    public Location(org.bukkit.Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
    }

    public org.bukkit.Location toLocation() {
        World world = Bukkit.getWorld(this.world);
        if (world == null) {
            return null;
        } else {
            return new org.bukkit.Location(world, this.x, this.y, this.z);
        }
    }

    public String toStorageString() {
        return this.x+","+this.y+","+this.z+","+this.world;
    }

}
