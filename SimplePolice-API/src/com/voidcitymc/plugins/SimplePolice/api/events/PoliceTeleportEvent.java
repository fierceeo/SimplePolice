package com.voidcitymc.plugins.SimplePolice.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PoliceTeleportEvent extends GenericSimplePoliceEvent {
    void onPoliceTp(Player police, Player targetedPlayer, Location teleportLocation);
}
