package me.semakon.commandExecutors;

import me.semakon.Handlers.EditTitleCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Exceptions.CannotRemoveDefaultCategoryException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Author:  Martijn
 * Date:    10-2-2016
 */
public class EditTitleExecutor {

    private DataContainer dataContainer;

    public EditTitleExecutor(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
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
                    // /titles create title <name> <description> [<category>]
                    if (args.length == 4 && args[1].equalsIgnoreCase("title")) {
                        String title = Utils.setColors(args[2]);
                        if (EditTitleCommands.createTitle(dataContainer, title, args[3], null)) {
                            Utils.sendMsg(sender, String.format("Added new title: %s%s%s.", ChatColor.ITALIC, title, ChatColor.RESET));
                        } else Utils.sendError(sender, "That title already exists, or that category doesn't exist.");

                    } else if (args.length == 5 && args[1].equalsIgnoreCase("title")) {
                        String title = Utils.setColors(args[2]);
                        if (EditTitleCommands.createTitle(dataContainer, title, args[3], args[4])) {
                            Utils.sendMsg(sender, String.format("Added new title: %s%s%s.", ChatColor.ITALIC, title, ChatColor.RESET));
                        } else Utils.sendError(sender, "That title already exists.");

                    // /titles create category <name> <description>
                    } else if (args.length == 4 && args[1].equalsIgnoreCase("category")) {
                        String category = Utils.setColors(args[2]);
                        if (EditTitleCommands.createCategory(dataContainer, category, args[3])) {
                            Utils.sendMsg(sender, String.format("Added new category: %s%s%s.", ChatColor.ITALIC, category, ChatColor.RESET));
                        } else Utils.sendError(sender, "That category already exists.");
                    } else return false;
                    return true;

                case "remove":
                    // /titles remove title <title>
                    if (args.length == 3 && args[1].equalsIgnoreCase("title")) {
                        String title = Utils.setColors(args[2]);
                        if (EditTitleCommands.removeTitle(dataContainer, title)) {
                            Utils.sendMsg(sender, "Removed " + ChatColor.ITALIC + title + ".");
                        } else Utils.sendError(sender, "That title doesn't exist.");
                        return true;

                    // /titles remove category <category>
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("category")) {
                        String title = Utils.setColors(args[2]);
                        try {
                            if (EditTitleCommands.removeCategory(dataContainer, title)) {
                                Utils.sendMsg(sender, "Removed " + ChatColor.ITALIC + title + ".");
                            } else Utils.sendError(sender, "That category doesn't exist.");
                        } catch (CannotRemoveDefaultCategoryException e) {
                            Utils.sendError(sender, e.getMessage());
                        }
                        return true;
                    }
                    return false;
                case "edit":
                    if (args.length == 5 && args[1].equalsIgnoreCase("title")) {
                        String title = Utils.setColors(args[2]);
                        String type = args[3];
                        String typeValue = args[4];

                        // /titles edit title <title> description <description>
                        if (type.equalsIgnoreCase("description")) {
                            if (EditTitleCommands.editDescription(dataContainer, title, typeValue)) {
                                Utils.sendMsg(sender, String.format("Changed description of %s%s%s to %s%s%s.", ChatColor.ITALIC, title,
                                        ChatColor.RESET, ChatColor.ITALIC, typeValue, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title doesn't exist.");
                            return true;

                        // /titles edit title <title> category <category>
                        } else if (type.equalsIgnoreCase("category")) {
                            if (EditTitleCommands.editCategory(dataContainer, title, typeValue)) {
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
                            String title = Utils.setColors(typeValue);
                            String newTitle = Utils.setColors(newName);
                            if (EditTitleCommands.renameTitle(dataContainer, title, newTitle)) {
                                Utils.sendMsg(sender, String.format("Renamed %s%s%s to %s%s%s.", ChatColor.ITALIC, title,
                                        ChatColor.RESET, ChatColor.ITALIC, newTitle, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title doesn't exist.");
                            return true;

                        // /titles rename category <category> <newName>
                        } else if (type.equalsIgnoreCase("category")) {
                            if (EditTitleCommands.renameCategory(dataContainer, typeValue, newName)) {
                                Utils.sendMsg(sender, String.format("Renamed %s%s%s to %s%s%s.", ChatColor.ITALIC, typeValue,
                                        ChatColor.RESET, ChatColor.ITALIC, newName, ChatColor.RESET));
                            } else Utils.sendError(sender, "That category doesn't exist."); // Real reason: There are no titles in the config.
                            return true;
                        }
                    }
                    return false;
            }

        return false;
    }

}
