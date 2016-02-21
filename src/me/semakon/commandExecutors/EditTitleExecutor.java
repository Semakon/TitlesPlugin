package me.semakon.commandExecutors;

import me.semakon.Handlers.EditTitleCommands;
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
public class EditTitleExecutor {

    private TitlesPlugin plugin;

    public EditTitleExecutor(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a command is executed.
     * @param sender Sender of the command.
     * @param args Arguments given with the command.
     * @return True if the command has been executed successfully.
     */
    public boolean execute(CommandSender sender, String[] args) {
            switch (args[0].toLowerCase()) {

                case "create":
                    // /titles create title <name> <description> <category>
                    if (args.length == 5 && args[1].equalsIgnoreCase("title")) {
                        if (EditTitleCommands.createTitle(plugin, args[2], args[3], args[4])) {
                            Utils.sendMsg(sender, String.format("Added new title: %s%s%s.", ChatColor.ITALIC, args[2], ChatColor.RESET));
                        } else Utils.sendError(sender, "That title already exists.");
                        return true;
                    }
                    return false;
                case "remove":
                    // /titles remove title <title>
                    if (args.length == 3 && args[1].equalsIgnoreCase("title")) {
                        if (EditTitleCommands.removeTitle(plugin, args[2])) {
                            Utils.sendMsg(sender, "Removed " + ChatColor.ITALIC + args[2] + ".");
                        } else Utils.sendError(sender, "That title doesn't exist.");
                        return true;

                    // /titles remove category <category>
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("category")) {
                        if (EditTitleCommands.removeCategory(plugin, args[2])) {
                            Utils.sendMsg(sender, "Removed " + ChatColor.ITALIC + args[2] + ".");
                        } else Utils.sendError(sender, "That category doesn't exist.");
                        return true;
                    }
                    return false;
                case "edit":
                    if (args.length == 5 && args[1].equalsIgnoreCase("title")) {
                        String title = args[2];
                        String type = args[3];
                        String typeValue = args[4];

                        // /titles edit title <title> description <description>
                        if (type.equalsIgnoreCase("description")) {
                            if (EditTitleCommands.editDescription(plugin, title, typeValue)) {
                                Utils.sendMsg(sender, String.format("Changed description of %s%s%s to %s%s%s.", ChatColor.ITALIC, title,
                                        ChatColor.RESET, ChatColor.ITALIC, typeValue, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title doesn't exist.");
                            return true;

                        // /titles edit title <title> category <category>
                        } else if (type.equalsIgnoreCase("category")) {
                            if (EditTitleCommands.editCategory(plugin, title, typeValue)) {
                                Utils.sendMsg(sender, String.format("Changed category of %s%s%s to %s%s%s.", ChatColor.ITALIC, title,
                                        ChatColor.RESET, ChatColor.ITALIC, typeValue, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title doesn't exist.");
                            return true;
                        }
                    }
                    return false;
                case "rename":
                    if (args.length == 4) {
                        String type = args[1];
                        String typeValue = args[2];
                        String newName = args[3];

                        // /titles rename title <title> <newName>
                        if (type.equalsIgnoreCase("title")) {
                            if (EditTitleCommands.renameTitle(plugin, typeValue, newName)) {
                                Utils.sendMsg(sender, String.format("Renamed %s%s%s to %s%s%s.", ChatColor.ITALIC, typeValue,
                                        ChatColor.RESET, ChatColor.ITALIC, newName, ChatColor.RESET));
                            }
                            return true;

                        // /titles rename category <category> <newName>
                        } else if (type.equalsIgnoreCase("category")) {
                            if (EditTitleCommands.renameCategory(plugin, typeValue, newName)) {
                                Utils.sendMsg(sender, String.format("Renamed %s%s%s to %s%s%s.", ChatColor.ITALIC, typeValue,
                                        ChatColor.RESET, ChatColor.ITALIC, newName, ChatColor.RESET));
                            }
                            return true;
                        }
                    }
                    return false;
            }

        return false;
    }

}
