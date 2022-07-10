package com.voidcitymc.plugins.SimplePolice.apiInternals;

import com.voidcitymc.plugins.SimplePolice.DatabaseUtility;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Utility implements com.voidcitymc.plugins.SimplePolice.api.Utility {

        public void payPoliceOnArrest(Player police, Player criminal) {
                com.voidcitymc.plugins.SimplePolice.Utility.payPoliceOnArrest(police, criminal);
        }

        public void takeMoneyOnArrest(Player criminal) {
                com.voidcitymc.plugins.SimplePolice.Utility.takeMoneyOnArrest(criminal);
        }

        public String leastCommonElement(String[] array) {
                return com.voidcitymc.plugins.SimplePolice.Utility.leastCommonElement(array);
        }

        public boolean inSafeArea(Player player) {
                return com.voidcitymc.plugins.SimplePolice.Utility.inSafeArea(player);
        }

        public ItemStack createGuiItem(final Material material, final String name, final String... lore) {
                return com.voidcitymc.plugins.SimplePolice.Utility.createGuiItem(material, name, lore);
        }

        public void addJail(String jailName, Location location) {
                DatabaseUtility.addJail(jailName, location);
        }

        public void removeJail(String jailName) {
                DatabaseUtility.removeJail(jailName);
        }

        public Location getJailLocation(String jailName) {
                return Jail.getJailLocation(jailName);
        }

        public ArrayList<String> jailList() {
                return com.voidcitymc.plugins.SimplePolice.Utility.jailList();
        }

        public ArrayList<UUID> jailedPlayers() {
                return Jail.jailedPlayers();
        }

        public boolean isJailed(UUID player) {
                return Jail.isJailed(player);
        }

        public String getPlayerCurrentJail(UUID player) {
                return Jail.getPlayerCurrentJail(player);
        }

        public double sentenceLength(UUID player) {
                return Jail.getSentenceLength(player);
        }

        public double sentenceLengthLeft(UUID player) {
                return Jail.timeLeft(player);
        }

        public void addPolice(String uuid) {
                DatabaseUtility.addPolice(uuid);
        }

        public void removePolice(String uuid) {
                DatabaseUtility.removePolice(uuid);
        }

        public boolean isPolice(String uuid) {
                return com.voidcitymc.plugins.SimplePolice.Utility.isPolice(uuid);
        }

        public boolean arePoliceOnline() {
                return com.voidcitymc.plugins.SimplePolice.Utility.arePoliceOnline();
        }

        public ArrayList<UUID> onlinePoliceList() {
                return com.voidcitymc.plugins.SimplePolice.Utility.onlinePoliceList();
        }

        public void addContrabandItem(ItemStack itemStack) {
                com.voidcitymc.plugins.SimplePolice.Utility.addContrabandItem(itemStack);
        }

        public void removeContrabandItem(ItemStack itemStack) {
                com.voidcitymc.plugins.SimplePolice.Utility.removeContrabandItem(itemStack);
        }

        public boolean isContraband(ItemStack itemStack) {
                return com.voidcitymc.plugins.SimplePolice.Utility.isContraband(itemStack);
        }

        public boolean isLocationSafe(Location location) {
                return com.voidcitymc.plugins.SimplePolice.Utility.isLocationSafe(location);
        }

        public Location policeTp(Player player, int farthestTpDistance) {
                return com.voidcitymc.plugins.SimplePolice.Utility.policeTp(player, farthestTpDistance);
        }

}
