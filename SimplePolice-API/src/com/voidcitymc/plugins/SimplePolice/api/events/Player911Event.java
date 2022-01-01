package com.voidcitymc.plugins.SimplePolice.api.events;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import org.bukkit.entity.Player;

public interface Player911Event extends SimplePoliceEvent {
    void on911(Player caller, String message);
}
