package me.semakon.commandExecutors;

import me.semakon.Handlers.SetCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    10-2-2016
 */
public class SetExecutor {

    private TitlesPlugin plugin;

    public SetExecutor(TitlesPlugin plugin) {
        this.plugin = plugin;
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
                    String response;
                    if (args.length == 3 && args[1].equalsIgnoreCase("title")) {
                       String title = args[2];
                        if (SetCommands.setTitle(plugin, player, title)) {
                            response = "Title set to %s" + title + "%s.";
                        } else response = "You don't own that title or it doesn't exist.";
                        Utils.sendMsg(sender, String.format(response, ChatColor.ITALIC, ChatColor.RESET));

                    // set title
                    } else if (args.length == 2 && args[1].equalsIgnoreCase("title")) {
                        if (SetCommands.disableTitle(plugin, player)) {
                            response = "Title disabled.";
                        } else response = "Player doesn't have that title active.";
                        Utils.sendMsg(sender, String.format(response, ChatColor.ITALIC, ChatColor.RESET));
                    } else return false;

                } else Utils.consolePrint("Only players can use the \"set\" command.");
                return true;
            case "user":
                String user = args[1];
                OfflinePlayer offlinePlayer = Utils.getOfflinePlayer(user);
                // check if player exists
                if (offlinePlayer == null) {
                    if (player == null) Utils.sendError(sender, "Player can't be found.");
                    else Utils.sendError(player, "Player can't be found.");
                    return true;
                }
                // /titles user <user> [add:remove:set] title <title>
                if (args.length == 5) {
                    if (args[3].equalsIgnoreCase("title")) {
                        String title = args[4];
                        String response = "%s%s";

                        if (args[2].equalsIgnoreCase("add")) {                                      // user <user> add title <title>
                            if (SetCommands.addTitle(plugin, offlinePlayer, title)) {
                                response = "Added %s" + title + "%s to " + offlinePlayer.getName() + "'s owned titles.";
                            } else response = "Title doesn't exist or player already has that title.";

                        } else if (args[2].equalsIgnoreCase("remove")) {                            // user <user> remove title <title>
                            if (SetCommands.removeTitle(plugin, offlinePlayer, title)) {
                                response = "Removed %s" + title + "%s from " + offlinePlayer.getName() + "'s owned titles.";
                            } else response = "Player doesn't own that title.";

                        } else if (args[2].equalsIgnoreCase("set")) {                               // user <user> set title <title>
                            Player p = offlinePlayer.getPlayer();
                            if (p != null) {
                                if (SetCommands.setTitle(plugin, p, title)) {
                                    response = "Title set to %s" + title + "%s.";
                                } else response = "You don't own that title or it doesn't exist.";
                            } else response = "Player is not online.";
                        }
                        if (player == null) Utils.consolePrint(String.format(response, "", ""));
                        else Utils.sendMsg(sender, String.format(response, ChatColor.ITALIC, ChatColor.RESET));
                    }
                }
                return true;
        }

        return false;
    }

}
