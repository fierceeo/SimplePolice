package com.voidcitymc.plugins.SimplePolice.api.events;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlayerUnjailEvent extends SimplePoliceEvent {
    void onPlayerUnjail(Player unjailedPlayer, String jailName, Location releaseLocation);
}
