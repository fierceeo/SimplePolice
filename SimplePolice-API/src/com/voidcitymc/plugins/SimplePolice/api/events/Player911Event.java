package com.voidcitymc.plugins.SimplePolice.api.events;

import org.bukkit.entity.Player;

public interface Player911Event extends GenericSimplePoliceEvent {
    void on911(Player caller, String message);
}
