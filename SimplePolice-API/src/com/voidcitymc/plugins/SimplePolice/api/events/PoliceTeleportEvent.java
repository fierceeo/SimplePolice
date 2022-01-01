package com.voidcitymc.plugins.SimplePolice.api.events;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PoliceTeleportEvent extends SimplePoliceEvent {
    void onPoliceTp(Player police, Player targetedPlayer, Location teleportLocation);
}
