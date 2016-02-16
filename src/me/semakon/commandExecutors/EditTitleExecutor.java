package me.semakon.commandExecutors;

import me.semakon.Handlers.EditCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    10-2-2016
 */
public class EditTitleExecutor implements CommandExecutor {

    private TitlesPlugin plugin;

    public EditTitleExecutor(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a command is executed.
     * @param sender Sender of the command.
     * @param cmd Command executed.
     * @param label Alias of the command which was used.
     * @param args Arguments given with the command.
     * @return True if the command has been executed successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean result = false;
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null || player.hasPermission(plugin.pm)) {
            switch (cmd.getName()) {

                case Utils.CREATE_NEW_TITLE:
                    if (args.length == 3) {
                        result = EditCommands.createTitle(plugin, args[0], args[1], args[2]);
                        if (player != null) {
                            if (result) {
                                player.sendMessage(String.format("Added new title: %s%s%s.", ChatColor.ITALIC, args[0], ChatColor.RESET));
                            } else {
                                Utils.sendError(player, "That title already exists.");
                            }
                        }
                    }
                    break;

                case Utils.REMOVE_TITLE:
                    if (args.length == 1) {
                        result = EditCommands.removeTitle(plugin, args[0]);
                        if (player != null) {
                            if (result) {
                                player.sendMessage(String.format("Removed %s%s%s.", ChatColor.ITALIC, args[0], ChatColor.RESET));
                            } else {
                                Utils.sendError(player, "That title already exists.");
                            }
                        }
                    }
                    break;

                case Utils.EDIT_DESCRIPTION:
                    if (args.length == 2) {
                        result = EditCommands.editDescription(plugin, args[0], args[1]);
                        if (player != null) {
                            if (result) {
                                player.sendMessage(String.format("Changed description of %s%s%s to %s%s%s", ChatColor.ITALIC, args[0],
                                        ChatColor.RESET, ChatColor.ITALIC, Utils.addSpaces(args[1]), ChatColor.RESET));
                            } else {
                                Utils.sendError(player, "That title doesn't exist.");
                            }
                        }
                    }
                    break;

            }
        } else player.sendMessage(ChatColor.RED + "You don't have permission to perform this action!");
        return result;
    }

}
