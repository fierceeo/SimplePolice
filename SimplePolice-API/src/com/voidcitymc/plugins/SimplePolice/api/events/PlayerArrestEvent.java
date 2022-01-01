package com.voidcitymc.plugins.SimplePolice.api.events;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlayerArrestEvent extends SimplePoliceEvent {
    void onPlayerArrest(Player police, Player arrestedPlayer, Location arrestLocation);
}
