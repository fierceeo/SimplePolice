package com.voidcitymc.plugins.SimplePolice.api.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerFriskEvent extends GenericSimplePoliceEvent {
    void onPlayerFrisk(Player police, Player arrestedPlayer, ItemStack[] contrabandItemsFound);
}
