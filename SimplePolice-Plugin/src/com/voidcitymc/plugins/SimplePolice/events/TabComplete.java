package com.voidcitymc.plugins.SimplePolice.events;

import com.voidcitymc.plugins.SimplePolice.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;

//TODO: clean code
public class TabComplete implements Listener {

    //event handler
    @EventHandler
    public void onTab(TabCompleteEvent event) {
        if (event.getBuffer().split(" ")[0].replaceFirst("/", "").equalsIgnoreCase("police")) {
            String[] buffer = event.getBuffer().split(" ");
            ArrayList<String> completions = new ArrayList<>();
            CommandSender sender = event.getSender();
            boolean isPolice = false;

            if (sender instanceof Player) {
                isPolice = Utility.isPolice(((Player) sender).getUniqueId().toString());
            }

            if (sender.hasPermission("SimplePolice.cmd.admin.reload")) {
                completions = cmdCompletePlayer(completions, "police admin reload", buffer, false);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.frisk.add")) {
                completions = cmdCompletePlayer(completions, "police admin frisk add", buffer, false);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.frisk.remove")) {
                completions = cmdCompletePlayer(completions, "police admin frisk remove", buffer, false);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.frisk.list")) {
                completions = cmdCompletePlayer(completions, "police admin frisk list", buffer, false);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.police.add")) {
                completions = cmdCompletePlayer(completions, "police admin police add", buffer, true);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.police.remove")) {
                completions = cmdCompletePlayer(completions, "police admin police remove", buffer, true);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.police.list")) {
                completions = cmdCompletePlayer(completions, "police admin police list", buffer, true);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.jails.add")) {
                completions = cmdCompletePlayer(completions, "police admin jail add", buffer, false);
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.jails.remove")) {
                completions = cmdCompletePlayer(completions, "police admin jail remove", buffer, false, Utility.jailList());
            }
            if (sender.hasPermission("SimplePolice.cmd.admin.jails.list")) {
                completions = cmdCompletePlayer(completions, "police admin jail list", buffer, false, Utility.jailList());
            }
            if (sender.hasPermission("SimplePolice.cmd.jail")) {
                completions = cmdCompletePlayer(completions, "police jail", buffer, true);
            }
            if (sender.hasPermission("police.cmd.tp") || isPolice) {
                completions = cmdCompletePlayer(completions, "police tp", buffer, true);
            }
            if (sender.hasPermission("police.cmd.release") || isPolice) {
                completions = cmdCompletePlayer(completions, "police release", buffer, true);
            }
            if (sender.hasPermission("police.cmd.chat") || isPolice) {
                completions = cmdCompletePlayer(completions, "police chat on", buffer, false);
                completions = cmdCompletePlayer(completions, "police chat off", buffer, false);
            }
            completions = cmdCompletePlayer(completions, "police help", buffer, false);
            event.setCompletions(completions);
        }
    }

    private ArrayList<String> cmdCompletePlayer(ArrayList<String> listToAddTo, String command, String[] buffer, boolean addAllOnlinePlayers) {
        String[] cmd = ("/" + command).split(" ");
        int maxLength = Math.min(cmd.length, buffer.length)-1;
        if (!(buffer.length == 0)) {
            if (!cmd[maxLength].equalsIgnoreCase(buffer[maxLength]) && cmd[maxLength].startsWith(buffer[maxLength])) {
                listToAddTo.add(cmd[maxLength]);
            } else if (cmd[maxLength].equalsIgnoreCase(buffer[maxLength]) && (cmd.length > maxLength+1)) {
                listToAddTo.add(cmd[maxLength+1]);
            }
        }
        if (buffer.length+1 > cmd.length && cmd[maxLength].equalsIgnoreCase(buffer[maxLength]) && addAllOnlinePlayers) {
            Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            int i = 0;
            while (i < onlinePlayers.length) {
                listToAddTo.add(onlinePlayers[i].getName());
                i++;
            }
        }
        return listToAddTo;
    }

    private ArrayList<String> cmdCompletePlayer(ArrayList<String> listToAddTo, String command, String[] buffer, boolean addAllOnlinePlayers, ArrayList<String> extraItemsToAdd) {
        String[] cmd = ("/" + command).split(" ");
        ArrayList<String> returnList = cmdCompletePlayer(listToAddTo, command, buffer, addAllOnlinePlayers);
        if (buffer.length >= cmd.length && cmd[cmd.length-1].equalsIgnoreCase(buffer[cmd.length-1])) {
            returnList.addAll(extraItemsToAdd);
        }
        return returnList;
    }
}
