package com.voidcitymc.plugins.SimplePolice.cmd;

import com.voidcitymc.plugins.SimplePolice.DatabaseUtility;
import com.voidcitymc.plugins.SimplePolice.LegacyUtils;
import com.voidcitymc.plugins.SimplePolice.SimplePolice;
import com.voidcitymc.plugins.SimplePolice.apiInternals.EventManager;
import com.voidcitymc.plugins.SimplePolice.config.Config;
import com.voidcitymc.plugins.SimplePolice.config.ConfigValues;
import com.voidcitymc.plugins.SimplePolice.events.Jail;
import com.voidcitymc.plugins.SimplePolice.Utility;
import com.voidcitymc.plugins.SimplePolice.events.PoliceChat;
import com.voidcitymc.plugins.SimplePolice.frisk.Frisk;
import com.voidcitymc.plugins.SimplePolice.gui.JailGUI;
import com.voidcitymc.plugins.SimplePolice.messages.Messages;
import me.zombie_striker.customitemmanager.CustomBaseObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Police implements Listener, CommandExecutor {

    //TODO: cleanup code

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = null;
        boolean isPlayer = false;
        boolean isPolice = false;
        String[] cmdArguments = new String[5];
        for (int i = 0; i < args.length; i++) {
            cmdArguments[i] = args[i].toLowerCase();
        }
        for (int i = args.length; i < cmdArguments.length; i++) {
            cmdArguments[i] = "";
        }
        if (sender instanceof Player) {
            player = (Player) sender;
            isPlayer = true;
            isPolice = Utility.isPolice(player.getUniqueId().toString());
        }

        switch (cmdArguments[0]) {
            case "admin":
                switch (cmdArguments[1]) {
                    case "reload":
                        if (sender.hasPermission("SimplePolice.cmd.admin.reload")) {
                            reloadConfig(sender);
                        } else {
                            invalidCommand(sender);
                        }
                        break;
                    case "frisk":
                        switch (cmdArguments[2]) {
                            case "add":
                                if (sender.hasPermission("SimplePolice.cmd.admin.frisk.add") && isPlayer) {
                                    friskListAdd(player);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            case "remove":
                                if (sender.hasPermission("SimplePolice.cmd.admin.frisk.remove") && isPlayer) {
                                    friskListRemove(player);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            case "list":
                                if (sender.hasPermission("SimplePolice.cmd.admin.frisk.list")) {
                                    friskList(sender);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            default:
                                invalidCommand(sender);
                                break;
                        }
                        break;
                    case "police":
                        switch (cmdArguments[2]) {
                            case "add":
                                if (sender.hasPermission("SimplePolice.cmd.admin.police.add")) {
                                    policeAdd(sender, cmdArguments[3]);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            case "remove":
                                if (sender.hasPermission("SimplePolice.cmd.admin.police.remove")) {
                                    policeRemove(sender, cmdArguments[3]);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            case "list":
                                if (sender.hasPermission("SimplePolice.cmd.admin.police.list")) {
                                    policeList(sender);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            default:
                                invalidCommand(sender);
                                break;
                        }
                        break;
                    case "jail":
                        switch (cmdArguments[2]) {
                            case "add":
                                if (sender.hasPermission("SimplePolice.cmd.admin.jails.add") && isPlayer) {
                                    addJail(player, cmdArguments[3]);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            case "remove":
                                if (sender.hasPermission("SimplePolice.cmd.admin.jails.remove")) {
                                    removeJail(sender, cmdArguments[3]);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            case "list":
                                if (sender.hasPermission("SimplePolice.cmd.admin.jails.list")) {
                                    listJail(sender);
                                } else {
                                    invalidCommand(sender);
                                }
                                break;
                            default:
                                invalidCommand(sender);
                                break;
                        }
                        break;
                    case "help":
                    case "":
                        if (sender.hasPermission("SimplePolice.cmd.admin.help")) {
                            adminHelp(sender);
                        }
                        break;
                    default:
                        invalidCommand(sender);
                        break;
                }
                break;
            case "jail":
                if (sender.hasPermission("SimplePolice.cmd.jail")) {
                    jailPlayer(sender, cmdArguments[1], cmdArguments[2], cmdArguments[3]);
                } else {
                    invalidCommand(sender);
                }
                break;
            case "tp":
                if ((sender.hasPermission("SimplePolice.cmd.tp") || isPolice) && isPlayer) {
                    policeTp(player, cmdArguments[1]);
                } else {
                    invalidCommand(sender);
                }
                break;
            case "release":
                if (sender.hasPermission("SimplePolice.cmd.release") || isPolice) {
                    if (cmdArguments[1].equalsIgnoreCase("all")) {
                        releaseAll(sender);
                    } else {
                        releasePlayer(sender, cmdArguments[1]);
                    }
                } else {
                    invalidCommand(sender);
                }
                break;
            case "chat":
                if ((sender.hasPermission("SimplePolice.cmd.chat") || isPolice) && isPlayer) {
                    switch (cmdArguments[1]) {
                        case "on":
                            policeChatOn(player);
                            break;
                        case "off":
                            policeChatOff(player);
                            break;
                        case "":
                            policeChatToggle(player);
                            break;
                        default:
                            invalidCommand(sender);
                            break;
                    }
                } else {
                    invalidCommand(sender);
                }
                break;
            case "help":
            case "":
                help(sender, isPolice);
                break;
            default:
                invalidCommand(sender);
                break;
        }
        return true;
    }

    private void invalidCommand(CommandSender sender) {
        sender.sendMessage(Messages.getMessage("InvalidCommand"));
    }

    private void reloadConfig(CommandSender sender) {
        Config config = new Config("SimplePolice.properties");
        config.setupConfig();
        ConfigValues.initialize(config);
        Config messagesConfig = new Config("Messages.properties");
        messagesConfig.setupConfig();
        Messages.initialize(messagesConfig);
        sender.sendMessage(Messages.getMessage("AdminConfigReload"));
    }

    private void friskListAdd(Player player) {
        if (!Utility.isContraband(LegacyUtils.getItemInMainHand(player.getInventory()))) {
            if (!LegacyUtils.getItemInMainHand(player.getInventory()).getType().equals(Material.AIR)) {
                Utility.addContrabandItem(LegacyUtils.getItemInMainHand(player.getInventory()));
                player.sendMessage(Messages.getMessage("AdminAddItem"));
            } else {
                player.sendMessage(Messages.getMessage("AdminAddItemFail"));
            }
        } else {
            player.sendMessage(Messages.getMessage("AdminAddItemFailContraband"));
        }
    }

    private void friskListRemove(Player player) {
        if (!LegacyUtils.getItemInMainHand(player.getInventory()).getType().equals(Material.AIR)) {
            Utility.removeContrabandItem(LegacyUtils.getItemInMainHand(player.getInventory()));
            player.sendMessage(Messages.getMessage("AdminRemoveItem"));
        } else {
            player.sendMessage(Messages.getMessage("AdminRemoveItemFail"));
        }
    }

    private void friskList(CommandSender sender) {
        ArrayList<String> textToReturn = new ArrayList<>();
        textToReturn.add(Messages.getMessage("PoliceAdminFriskList"));

        for (Utility.LoreItemStackPair item: DatabaseUtility.getContrabandItems()) {

            if (item.itemStack.getItemMeta().getDisplayName() != null && !item.itemStack.getItemMeta().getDisplayName().equals("")) {
                textToReturn.add(ChatColor.DARK_AQUA + item.itemStack.getItemMeta().getDisplayName());
            } else {
                textToReturn.add(ChatColor.DARK_AQUA + Frisk.capitalize((item.itemStack.getType().toString().replace("_", " ")).toLowerCase()));
            }
        }

        if (SimplePolice.qaInstalled) {
            for (CustomBaseObject qaItem : DatabaseUtility.getContrabandQAItems()) {
                textToReturn.add(ChatColor.GOLD + Frisk.capitalize(qaItem.getDisplayName()));
            }
        }

        sender.sendMessage(textToReturn.toArray(new String[0]));
    }

    private void policeAdd(CommandSender sender, String playerToAdd) {
        if (!playerToAdd.equals("")) {
            if (Bukkit.getPlayer(playerToAdd) != null && (!Utility.isPolice(Bukkit.getPlayer(playerToAdd).getUniqueId().toString()))) {
                DatabaseUtility.addPolice(Bukkit.getPlayer(playerToAdd).getUniqueId().toString());
                sender.sendMessage(Messages.getMessage("PoliceOfficerAdd", playerToAdd));
            } else {
                sender.sendMessage(Messages.getMessage("PoliceOfficerAddFail", playerToAdd));
            }

        } else {
            sender.sendMessage(Messages.getMessage("PoliceOfficerAddNoPlayer"));
        }
    }

    private void policeRemove(CommandSender sender, String playerToRemove) {
        if (!playerToRemove.equals("")) {
            if (Bukkit.getPlayer(playerToRemove) != null && Utility.isPolice(Bukkit.getPlayer(playerToRemove).getUniqueId().toString())) {
                DatabaseUtility.removePolice(Bukkit.getPlayer(playerToRemove).getUniqueId().toString());
                sender.sendMessage(Messages.getMessage("PoliceOfficerRemove", playerToRemove));
            } else {
                sender.sendMessage(Messages.getMessage("PoliceOfficerRemoveFail", playerToRemove));
            }
        } else {
            sender.sendMessage(Messages.getMessage("PoliceOfficerRemoveNoPlayer"));
        }
    }

    private void addJail(Player player, String jailName) {
        if (!jailName.equals("")) {
            if (!Utility.jailList().contains(jailName)) {
                DatabaseUtility.addJail(jailName, player.getLocation());
                player.sendMessage(Messages.getMessage("PoliceAdminSetJailSuccess", jailName));
            } else {
                player.sendMessage(Messages.getMessage("PoliceAdminSetJailDuplicate"));
            }
        } else {
            player.sendMessage(Messages.getMessage("PoliceAdminSetJailNoJail"));
        }
    }

    private void removeJail(CommandSender sender, String jailName) {
        if (!jailName.equals("")) {
            DatabaseUtility.removeJail(jailName);
            sender.sendMessage(Messages.getMessage("PoliceAdminDelJailSuccess", jailName));
        } else {
            sender.sendMessage(Messages.getMessage("PoliceAdminDelJailNoJail"));
        }
    }

    private void jailPlayer(CommandSender sender, String player, String jail, String jailTime) {
        if (!player.equals("")) {
            if (Bukkit.getPlayerExact(player) != null) {
                if (!jail.equals("")) {
                    if (!DatabaseUtility.getJailLocations().isEmpty()) {
                        String jailName = jail;
                        if (Utility.jailList().contains(jailName.toLowerCase())) {
                            Player jailedPlayer = Bukkit.getPlayerExact(player);
                            if (!Jail.isJailed(jailedPlayer.getUniqueId())) {
                                if (jailedPlayer.isDead()) {
                                    jailedPlayer.spigot().respawn();
                                }
                                Jail.previousLocation.put(jailedPlayer.getUniqueId().toString(), jailedPlayer.getLocation());
                                jailedPlayer.teleport(Jail.getJailLocation(jailName));
                            }
                            if (sender instanceof Player) {
                                JailGUI.onPlayerArrest((Player) sender, jailedPlayer, jailName);
                                EventManager.runPlayerArrestEvent((Player) sender, jailedPlayer, Jail.previousLocation.get(jailedPlayer.getUniqueId().toString()));
                            } else {
                                double jailTimeDouble = 300;
                                try {
                                    jailTimeDouble = Double.parseDouble(jailTime);
                                } catch (NumberFormatException ignored) {
                                }
                                Jail.jailPlayer(jailedPlayer.getUniqueId(), jailTimeDouble, jail);
                            }
                        } else {
                            sender.sendMessage(Messages.getMessage("AdminJailNotExist"));
                        }
                    } else {
                        sender.sendMessage(Messages.getMessage("ArrestNoJails"));
                    }
                } else {
                    if (!DatabaseUtility.getJailLocations().isEmpty()) {
                        String jailName = Jail.getJail();
                        Player jailedPlayer = Bukkit.getPlayerExact(player);
                        if (!Jail.isJailed(jailedPlayer.getUniqueId())) {
                            if (jailedPlayer.isDead()) {
                                jailedPlayer.spigot().respawn();
                            }
                            Jail.previousLocation.put(jailedPlayer.getUniqueId().toString(), jailedPlayer.getLocation());
                            jailedPlayer.teleport(Jail.getJailLocation(jailName));
                        }
                        if (sender instanceof Player) {
                            JailGUI.onPlayerArrest((Player) sender, jailedPlayer, jailName);
                            EventManager.runPlayerArrestEvent((Player) sender, jailedPlayer, Jail.previousLocation.get(jailedPlayer.getUniqueId().toString()));
                        } else {
                            double jailTimeDouble = 300;
                            try {
                                jailTimeDouble = Double.parseDouble(jailTime);
                            } catch (NumberFormatException ignored) {
                            }
                            Jail.jailPlayer(jailedPlayer.getUniqueId(), jailTimeDouble, jail);
                        }
                    } else {
                        sender.sendMessage(Messages.getMessage("ArrestNoJails"));
                    }
                }
            } else {
                sender.sendMessage(Messages.getMessage("AdminJailPlayerOffline"));
            }

        } else {
            sender.sendMessage(Messages.getMessage("AdminJailSpecifyPlayer"));
        }
    }

    private void policeTp(Player police, String criminal) {
        if (!criminal.equals("")) {
            if (Bukkit.getPlayer(criminal) != null) {
                int MaxValTp = ConfigValues.maxPoliceTpDistance;
                Location policeTpLocation = Utility.policeTp(Bukkit.getPlayer(criminal), MaxValTp);
                police.teleport(policeTpLocation);
                EventManager.runPoliceTp(police, Bukkit.getPlayer(criminal), policeTpLocation);
                police.sendMessage(Messages.getMessage("PoliceTp"));
                Objects.requireNonNull(Bukkit.getPlayer(criminal)).sendMessage(Messages.getMessage("PoliceTpComingMessage"));
            } else {
                police.sendMessage(Messages.getMessage("PoliceTpPlayerOffline"));
            }
        } else {
            police.sendMessage(Messages.getMessage("PoliceSpecifyPlayer"));
        }
    }

    private void releasePlayer(CommandSender sender, String player) {
        if (Bukkit.getPlayerExact(player) != null) {
            if (Jail.isJailed(Bukkit.getPlayerExact(player).getUniqueId())) {
                Jail.unjailPlayer(Objects.requireNonNull(Bukkit.getPlayerExact(player)).getUniqueId(), true);
                sender.sendMessage(Messages.getMessage("UnjailPlayer", player));
            } else {
                sender.sendMessage(Messages.getMessage("UnjailPlayerNotJailed"));
            }
        } else {
            sender.sendMessage(Messages.getMessage("ErrorUnjailingPlayerOffline", player));
        }
    }

    private void releaseAll(CommandSender sender) {
        ArrayList<UUID> jailedPlayers = Jail.jailedPlayers();
        jailedPlayers.forEach(player -> {
            if (Bukkit.getPlayer(player) != null && Jail.isJailed(player)) {
                Jail.unjailPlayer(player, true);
            }
        });
        sender.sendMessage(Messages.getMessage("UnjailAllOnlinePlayers"));
    }

    private void policeChatOn(Player player) {
        PoliceChat.setToggled((player.getUniqueId().toString()));
        player.sendMessage(Messages.getMessage("PoliceChatToggleOn"));
    }

    private void policeChatOff(Player player) {
        PoliceChat.removeToggled(player.getUniqueId().toString());
        player.sendMessage(Messages.getMessage("PoliceChatToggleOff"));
    }

    private void policeChatToggle(Player player) {
        if (PoliceChat.isToggled(player.getUniqueId().toString())) {
            player.sendMessage(Messages.getMessage("PoliceChatToggleOff"));
        } else {
            player.sendMessage(Messages.getMessage("PoliceChatToggleOn"));
        }
        PoliceChat.toggleChat(player.getUniqueId().toString());
    }

    public void listJail(CommandSender sender) {
        ArrayList<String> textToReturn = new ArrayList<>(Utility.jailList());

        for (int i = 0; i<textToReturn.size(); i++) {
            textToReturn.set(i, ChatColor.DARK_AQUA+textToReturn.get(i));
        }

        textToReturn.add(0, Messages.getMessage("PoliceAdminJailList"));

        sender.sendMessage(textToReturn.toArray(new String[0]));
    }

    public void policeList(CommandSender sender) {
        ArrayList<String> textToReturn = new ArrayList<>();

        for (String uuid: DatabaseUtility.getPoliceUUIDList()) {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if (player != null) {
                textToReturn.add(Bukkit.getPlayer(UUID.fromString(uuid)).getName());
            } else {
                textToReturn.add(uuid);
            }
        }

        for (int i = 0; i<textToReturn.size(); i++) {
            textToReturn.set(i, ChatColor.DARK_AQUA+textToReturn.get(i));
        }

        textToReturn.add(0, Messages.getMessage("PoliceOfficerList"));

        sender.sendMessage(textToReturn.toArray(new String[0]));
    }

    private void adminHelp(CommandSender sender) {
        sender.sendMessage(Messages.getMessage("PoliceAdminHelpTitle"));
        sender.sendMessage(Messages.getMessage("PoliceAdminHelp1"));
        if (sender.hasPermission("SimplePolice.cmd.admin.reload")) {
            sender.sendMessage(Messages.getMessage("PoliceAdminHelpReload"));
        }
        if (sender.hasPermission("SimplePolice.cmd.admin.frisk.add") || sender.hasPermission("SimplePolice.cmd.admin.frisk.remove")) {
            sender.sendMessage(Messages.getMessage("PoliceAdminHelpFrisk"));
        }
        if (sender.hasPermission("SimplePolice.cmd.admin.frisk.list")) {
            sender.sendMessage(Messages.getMessage("PoliceAdminHelpFriskList"));
        }
        if (sender.hasPermission("SimplePolice.cmd.admin.police.add") || sender.hasPermission("SimplePolice.cmd.admin.police.remove") || sender.hasPermission("SimplePolice.cmd.admin.police.list")) {
            sender.sendMessage(Messages.getMessage("PoliceAdminHelpPolice"));
        }
        if (sender.hasPermission("SimplePolice.cmd.admin.jails.add") || sender.hasPermission("SimplePolice.cmd.admin.jails.remove") || sender.hasPermission("SimplePolice.cmd.admin.jails.list")) {
            sender.sendMessage(Messages.getMessage("PoliceAdminHelpJail"));
        }
    }

    private void help(CommandSender sender, boolean isPolice) {
        sender.sendMessage(Messages.getMessage("PoliceHelpTitle"));
        sender.sendMessage(Messages.getMessage("PoliceHelpCommands"));
        if (sender.hasPermission("police.cmd.tp") || isPolice) {
            sender.sendMessage(Messages.getMessage("PoliceHelpPoliceTp"));
        }
        if (sender.hasPermission("police.cmd.jail")) {
            sender.sendMessage(Messages.getMessage("PoliceHelpPoliceJail"));
        }
        if (sender.hasPermission("police.cmd.release") || isPolice) {
            sender.sendMessage(Messages.getMessage("PoliceHelpPoliceRelease"));
        }
        if (sender.hasPermission("police.cmd.chat") || isPolice) {
            sender.sendMessage(Messages.getMessage("PoliceHelpPoliceChat"));
        }
        if (sender.hasPermission("police.cmd.admin.help")) {
            sender.sendMessage(Messages.getMessage("PoliceHelpPoliceAdmin"));
        }
        sender.sendMessage(Messages.getMessage("PoliceHelpPoliceHelp"));
    }

}
