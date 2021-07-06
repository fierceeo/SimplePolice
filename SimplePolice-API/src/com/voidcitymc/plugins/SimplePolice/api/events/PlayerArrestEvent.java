package com.voidcitymc.plugins.SimplePolice.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlayerArrestEvent extends GenericSimplePoliceEvent {
    void onPlayerArrest(Player police, Player arrestedPlayer, Location arrestLocation);
}
