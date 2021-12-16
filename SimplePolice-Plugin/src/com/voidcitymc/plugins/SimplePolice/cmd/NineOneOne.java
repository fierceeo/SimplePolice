package com.voidcitymc.plugins.SimplePolice.cmd;

import com.voidcitymc.plugins.SimplePolice.Utility;
import com.voidcitymc.plugins.SimplePolice.apiInternals.EventManager;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;


public class NineOneOne implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ArrayList<UUID> onlinePoliceList = Utility.onlinePoliceList();

            if (onlinePoliceList.size() > 0) {
                StringBuilder nineOneOneMessage = new StringBuilder();
                for (String argsElement: args) {
                    nineOneOneMessage.append(argsElement+" ");
                }

                if (nineOneOneMessage.length() > 0) {
                    nineOneOneMessage.deleteCharAt(nineOneOneMessage.length() - 1);
                }

                EventManager.runPlayer911Event(player, nineOneOneMessage.toString());

                Iterator<UUID> onlinePoliceIterator = onlinePoliceList.iterator();
                while (onlinePoliceIterator.hasNext()) {
                    Player currentPolicePlayer = Bukkit.getPlayer(onlinePoliceIterator.next());
                    if (currentPolicePlayer != null) {
                        if (ConfigValues.showCords911) {
                            Location playerLocation = player.getLocation();
                            currentPolicePlayer.sendMessage(Messages.getMessage("NineOneOneMsgPoliceCords", player.getName(), playerLocation.getBlockX() + ", " + playerLocation.getBlockY() + ", " + playerLocation.getBlockZ(), playerLocation.getWorld().getName(), nineOneOneMessage.toString()));
                        } else {
                            currentPolicePlayer.sendMessage(Messages.getMessage("NineOneOneMsgPolice", player.getName(), nineOneOneMessage.toString()));
                        }
                    }
                }
                player.sendMessage(Messages.getMessage("NineOneOnePoliceOnline"));
            } else {
                player.sendMessage(Messages.getMessage("NineOneOneNoPolice"));
            }


            return true;
        } else {
            sender.sendMessage(Messages.getMessage("NineOneOneOnlyPlayersCanRunCMD"));
            return true;
        }
    }

}
