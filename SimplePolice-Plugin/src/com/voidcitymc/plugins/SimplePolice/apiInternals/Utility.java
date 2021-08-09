package com.voidcitymc.plugins.SimplePolice.apiInternals;

import com.voidcitymc.plugins.SimplePolice.Worker;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Utility implements com.voidcitymc.plugins.SimplePolice.api.Utility {

        public void payPoliceOnArrest(Player police, Player criminal) {
                Worker.payPoliceOnArrest(police, criminal);
        }

        public void takeMoneyOnArrest(Player criminal) {
                Worker.takeMoneyOnArrest(criminal);
        }

        public String leastCommonElement(String[] array) {
                return Worker.leastCommonElement(array);
        }

        public boolean inSafeArea(Player player) {
                return Worker.inSafeArea(player);
        }

        public ItemStack createGuiItem(final Material material, final String name, final String... lore) {
                return Worker.createGuiItem(material, name, lore);
        }

        public void addJail(String jailName, Location location) {
                Worker.addJail(jailName, location);
        }

        public void removeJail(String jailName) {
                Worker.removeJail(jailName);
        }

        public Location getJailLocation(String jailName) {
                return Jail.getJailLocation(jailName);
        }

        public ArrayList<String> jailList() {
                return Worker.jailList();
        }

        public ArrayList<UUID> jailedPlayers() {
                return Jail.jailedPlayers();
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
                Worker.addPolice(uuid);
        }

        public void removePolice(String uuid) {
                Worker.removePolice(uuid);
        }

        public boolean isPolice(String uuid) {
                return Worker.isPolice(uuid);
        }

        public boolean arePoliceOnline() {
                return Worker.arePoliceOnline();
        }

        public ArrayList<UUID> onlinePoliceList() {
                return Worker.onlinePoliceList();
        }

        public byte[] serializeItemStack(ItemStack itemStack) throws IOException {
                return Worker.serializeItemStack(itemStack);
        }

        public ItemStack deserializeItemStack(byte[] serializedItemStack) throws IOException, ClassNotFoundException {
                return Worker.deserializeItemStack(serializedItemStack);
        }

        public void addContrabandItem(ItemStack itemStack) {
                Worker.addContrabandItem(itemStack);
        }

        public void removeContrabandItem(ItemStack itemStack) {
                Worker.removeContrabandItem(itemStack);
        }

        public boolean isContraband(ItemStack itemStack) {
                return Worker.isContraband(itemStack);
        }

        public boolean isLocationSafe(Location location) {
                return Worker.isLocationSafe(location);
        }

        public Location policeTp(Player player, int farthestTpDistance) {
                return Worker.policeTp(player, farthestTpDistance);
        }

}
