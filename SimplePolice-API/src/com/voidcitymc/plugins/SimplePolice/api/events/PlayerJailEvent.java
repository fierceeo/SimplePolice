package com.voidcitymc.plugins.SimplePolice.api.events;

import org.bukkit.entity.Player;

public interface PlayerJailEvent extends GenericSimplePoliceEvent {
    void onPlayerJail(Player arrestedPlayer, String jailName, double jailTime);
}
