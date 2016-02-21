package me.semakon.commandExecutors;

import me.semakon.Handlers.RequestCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class RequestExecutor {

    private TitlesPlugin plugin;

    public RequestExecutor(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a command is executed.
     * @param sender Sender of the command.
     * @param args Arguments given with the command.
     * @return True if the command has been executed successfully.
     */
    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        if (player != null) {
            if (sender.hasPermission(plugin.makeRequestsPerm)) {

                // /titles request submit title <title>
                if (args.length == 4 && args[1].equalsIgnoreCase("submit")) {
                    String title = args[3];
                    if (RequestCommands.submitRequest(plugin, player, title)) {
                        Utils.sendMsg(sender, "Your request for " + ChatColor.ITALIC + title + ChatColor.RESET + " has been submitted.");
                    }
                    else Utils.sendError(sender, "You already have a pending request or that title doesn't exist.");
                    return true;
                }

                // /titles request retract
                if (args.length == 2 && args[1].equalsIgnoreCase("retract")) {
                    if (RequestCommands.retractRequest(plugin, player)) Utils.sendMsg(sender, "Your request has successfully been retracted.");
                    else Utils.sendError(sender, "You don't have a pending request.");
                    return true;
                }

            } else {
                Utils.sendError(sender, "You don't have permission to do that.");
                return true;
            }
        }
        if (sender.hasPermission(plugin.handleRequestsPerm)) {
            if (args.length == 4 && args[2].equalsIgnoreCase("user")) {

                OfflinePlayer user = Utils.getOfflinePlayer(args[3]);
                String type = args[1];
                if (user != null) {

                    // /titles request deny user <user>
                    if (type.equalsIgnoreCase("deny")) {
                        if (RequestCommands.denyRequest(plugin, user)) Utils.sendMsg(sender, "Request has been denied.");
                        else Utils.sendError(sender, "That player doesn't have a pending request.");
                        return true;

                    // /titles request deny user <user>
                    } else if (type.equalsIgnoreCase("approve")) {
                        if (RequestCommands.approveRequest(plugin, user)) Utils.sendMsg(sender, "Request has been approved.");
                        else Utils.sendError(sender, "That player doesn't have a pending request.");
                        return true;

                    // /titles request tp user <user>
                    } else if (type.equalsIgnoreCase("tp")) {
                        if (player == null) {
                            // command was send from console, console can't be teleported...
                            Utils.sendError(sender, "Only players can use that command.");
                            return true;
                        }
                        if (RequestCommands.tpToRequest(plugin, user, player)) {
                            Utils.sendMsg(sender, "You have been teleported to " + user.getName() + "'s submit location.");
                        } else Utils.sendError(sender, "That player doesn't have a pending request.");
                        return true;
                    }

                } else Utils.sendError(sender, "That player doesn't exist.");
            }
        } else {
            Utils.sendError(sender, "You don't have permission to do that.");
            return true;
        }
        return false;
    }
}
