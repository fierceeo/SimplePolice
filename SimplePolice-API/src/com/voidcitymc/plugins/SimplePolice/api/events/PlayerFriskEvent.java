package com.voidcitymc.plugins.SimplePolice.api.events;

import com.voidcitymc.plugins.SimplePolice.api.SimplePoliceEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerFriskEvent extends SimplePoliceEvent {
    void onPlayerFrisk(Player police, Player arrestedPlayer, ItemStack[] contrabandItemsFound);
}
