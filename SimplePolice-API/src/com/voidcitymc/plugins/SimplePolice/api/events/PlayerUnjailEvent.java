package com.voidcitymc.plugins.SimplePolice.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlayerUnjailEvent extends GenericSimplePoliceEvent {
    void onPlayerUnjail(Player unjailedPlayer, String jailName, Location releaseLocation);
}
