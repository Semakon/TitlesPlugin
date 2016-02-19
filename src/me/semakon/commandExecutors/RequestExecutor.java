package me.semakon.commandExecutors;

import me.semakon.Handlers.RequestCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

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
                String response;

                // /titles request submit title <title> <comments>
                if (args.length == 5 && args[1].equalsIgnoreCase("submit")) {
                    String title = args[3];
                    if (RequestCommands.submitRequest(plugin, player, title, args[4])) response = "Your request for " + ChatColor.ITALIC + title + ChatColor.RESET + " has been submitted.";
                    else response = "You already have a pending request or that title doesn't exist.";
                    Utils.sendMsg(sender, response);
                    return true;
                }

                // /titles request retract
                if (args.length == 2 && args[1].equalsIgnoreCase("retract")) {
                    if (RequestCommands.retractRequest(plugin, player)) response = "Your request has successfully been retracted.";
                    else response = "You don't have a pending request.";
                    Utils.sendMsg(sender, response);
                    return true;
                }

            } else {
                Utils.sendError(sender, "You don't have permission to do that.");
                return true;
            }
        }
        if (sender.hasPermission(plugin.handelRequestsPerm)) {
            if (args.length == 4) {
                OfflinePlayer user = Utils.getOfflinePlayer(args[3]);
                String type = args[1];
                if (user != null) {
                    String response;

                    // /titles request deny user <user>
                    if (type.equalsIgnoreCase("deny")) {
                        if (RequestCommands.denyRequest(plugin, user)) response = "Request has been denied.";
                        else response = "That player doesn't have a pending request.";
                        Utils.sendMsg(sender, response);
                        return true;

                    // /titles request deny user <user>
                    } else if (type.equalsIgnoreCase("approve")) {
                        if (RequestCommands.approveRequest(plugin, user)) response = "Request has been approved.";
                        else response = "That player doesn't have a pending request.";
                        Utils.sendMsg(sender, response);
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
