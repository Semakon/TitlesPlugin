package me.semakon.commandExecutors;

import me.semakon.Handlers.SetCommands;
import me.semakon.Utils;
import me.semakon.localStorage.DataContainer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    10-2-2016
 */
public class SetExecutor {

    private DataContainer dataContainer;

    public SetExecutor(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        String type = args[0];
        switch (type) {
            case "set":
                // /titles set title <title>
                if (player != null) {
                    // set title <title>
                    if (args.length == 3 && args[1].equalsIgnoreCase("title")) {
                        String title = Utils.setColors(args[2]);
                        if (SetCommands.setTitle(dataContainer, player, title)) {
                            Utils.sendMsg(sender, String.format("Title set to %s" + title + "%s.", ChatColor.ITALIC, ChatColor.RESET));
                        } else Utils.sendError(sender, "Player doesn't own that title or it doesn't exist.");

                    // set title
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("title")) {
                        if (SetCommands.disableTitle(dataContainer, player)) Utils.sendMsg(sender, "Title disabled.");
                        else Utils.sendError(sender, "You don't have an active title.");
                    } else return false;

                } else Utils.consolePrint("Only players can use the \"set\" command.");
                return true;
            case "user":
                String user = args[1];
                OfflinePlayer offlinePlayer = Utils.getOfflinePlayer(user);
                // check if player exists
                if (offlinePlayer == null) {
                    Utils.sendError(sender, "Player can't be found.");
                    return true;
                }
                // /titles user <user> [add:remove:set] title <title>
                if (args.length == 5) {
                    if (args[3].equalsIgnoreCase("title")) {
                        String title = Utils.setColors(args[4]);

                        if (args[2].equalsIgnoreCase("add")) {                                      // user <user> add title <title>
                            if (SetCommands.addTitle(dataContainer, offlinePlayer, title)) {
                                Utils.sendMsg(sender, String.format("Added %s" + title + "%s to " + offlinePlayer.getName() + "'s owned titles.", ChatColor.ITALIC, ChatColor.RESET));
                            } else Utils.sendError(sender, "Title doesn't exist or player already has that title.");

                        } else if (args[2].equalsIgnoreCase("remove")) {                            // user <user> remove title <title>
                            if (SetCommands.removeTitle(dataContainer, offlinePlayer, title)) {
                                Utils.sendMsg(sender, String.format("Removed %s" + title + "%s from " + offlinePlayer.getName() + "'s owned titles.", ChatColor.ITALIC, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title doesn't exist.");

                        } else if (args[2].equalsIgnoreCase("set")) {                               // user <user> set title <title>
                            Player p = offlinePlayer.getPlayer();
                            if (p != null) {
                                if (SetCommands.setTitle(dataContainer, p, title)) {
                                    Utils.sendMsg(sender, String.format("Title set to %s" + title + "%s.", ChatColor.ITALIC, ChatColor.RESET));
                                } else Utils.sendError(sender, "Player doesn't own that title or it doesn't exist.");
                            } else Utils.sendError(sender, "Player is not online.");
                        }
                    }
                } else {
                    return false;
                }
                return true;
        }

        return false;
    }

}
