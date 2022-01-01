package com.voidcitymc.plugins.SimplePolice.api.events;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import org.bukkit.entity.Player;

public interface PlayerJailEvent extends SimplePoliceEvent {
    void onPlayerJail(Player arrestedPlayer, String jailName, double jailTime);
}
