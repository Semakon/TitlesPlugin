package me.semakon.commandExecutors;

import me.semakon.Handlers.EditTitleCommands;
import me.semakon.Utils;
import me.semakon.localStorage.Category;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Exceptions.CannotRemoveDefaultCategoryException;
import me.semakon.localStorage.Exceptions.InvalidCategoryRuntimeException;
import me.semakon.localStorage.Title;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.IllegalFormatConversionException;

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
                    // /titles create title <name> <description>
                    try {
                        if (args.length == 4 && args[1].equalsIgnoreCase("title")) {
                            String title = Utils.setColors(args[2]);
                            String description = Utils.setColors(args[3]);

                            if (EditTitleCommands.createTitle(dataContainer, title, description, null)) {
                                Utils.sendMsg(sender, String.format("Added new title: %s%s%s.", ChatColor.ITALIC, title, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title already exists.");

                        // /titles create title <name> <description> <category>
                        } else if (args.length == 5 && args[1].equalsIgnoreCase("title")) {
                            String title = Utils.setColors(args[2]);
                            String description = Utils.setColors(args[3]);
                            String categoryId = Utils.strip(Utils.setColors(args[4]));

                            if (EditTitleCommands.createTitle(dataContainer, title, description, categoryId)) {
                                Utils.sendMsg(sender, String.format("Added new title: %s%s%s.", ChatColor.ITALIC, title, ChatColor.RESET));
                            } else Utils.sendError(sender, "That title already exists.");

                        // /titles create category <name> <description>
                        } else if (args.length == 4 && args[1].equalsIgnoreCase("category")) {
                            String category = Utils.setColors(args[2]);
                            String description = Utils.setColors(args[3]);

                            if (EditTitleCommands.createCategory(dataContainer, category, description)) {
                                Utils.sendMsg(sender, String.format("Added new category: %s%s%s.", ChatColor.ITALIC, category, ChatColor.RESET));
                            } else Utils.sendError(sender, "That category already exists.");
                        } else return false;
                    } catch (InvalidCategoryRuntimeException e) {
                        Utils.sendError(sender, "That category doesn't exist.");
                    }
                    return true;

                case "remove":
                    // /titles remove title <title>
                    if (args.length == 3 && args[1].equalsIgnoreCase("title")) {

                        // get title from string
                        Title title = dataContainer.getTitle(Utils.strip(Utils.setColors(args[2])));

                        // remove title or send error
                        if (title != null) {
                            dataContainer.removeTitle(title);
                            Utils.sendMsg(sender, "Removed " + ChatColor.ITALIC + title.getName() + ".");
                        } else Utils.sendError(sender, "That title doesn't exist.");
                        return true;

                    // /titles remove category <category>
                    } else if (args.length == 3 && args[1].equalsIgnoreCase("category")) {

                        // get category from string
                        Category category = dataContainer.getCategory(Utils.strip(Utils.setColors(args[2])));

                        // remove category or send error
                        try {
                            if (category != null) {
                                dataContainer.removeCategory(category);
                                Utils.sendMsg(sender, "Removed " + ChatColor.ITALIC + category.getName() + ".");
                            } else Utils.sendError(sender, "That category doesn't exist.");
                        } catch (CannotRemoveDefaultCategoryException e) {
                            Utils.sendError(sender, e.getMessage());
                        }
                        return true;
                    }
                    return false;
                case "edit":
                    if (args.length == 5 && args[1].equalsIgnoreCase("title")) {
                        Title title = dataContainer.getTitle(Utils.strip(Utils.setColors(args[2])));
                        if (title == null) {
                            Utils.sendError(sender, "That title doesn't exist.");
                            return true;
                        }

                        String type = args[3];
                        String typeValue = Utils.setColors(args[4]);

                        // /titles edit title <title> description <description>
                        if (type.equalsIgnoreCase("description")) {
                            dataContainer.editTitleDescription(title, typeValue);
                            Utils.sendMsg(sender, String.format("Changed description of %s%s%s to %s%s%s.", ChatColor.ITALIC, title.getName(),
                                    ChatColor.RESET, ChatColor.ITALIC, typeValue, ChatColor.RESET));

                        // /titles edit title <title> category <category>
                        } else if (type.equalsIgnoreCase("category")) {
                            //
                            Category category = dataContainer.getCategory(Utils.strip(typeValue));
                            if (category != null) {
                                dataContainer.editTitleCategory(title, category);
                                Utils.sendMsg(sender, String.format("Changed category of %s%s%s to %s%s%s.", ChatColor.ITALIC, title.getName(),
                                        ChatColor.RESET, ChatColor.ITALIC, category.getName(), ChatColor.RESET));
                            } else Utils.sendError(sender, "That category doesn't exist.");

                            // /titles edit title <title> unique <false|true>
                        } else if (type.equalsIgnoreCase("unique")) {
                            String unique = args[4];
                            if (unique.equalsIgnoreCase("true")) {
                                dataContainer.editTitleUnique(title, true);
                                Utils.sendMsg(sender, String.format("Changed %s%s%s to a %sunique%s title.", ChatColor.ITALIC, title.getName(),
                                        ChatColor.RESET, ChatColor.ITALIC, ChatColor.RESET));

                            } else if (unique.equalsIgnoreCase("true")) {
                                dataContainer.editTitleUnique(title, false);
                                Utils.sendMsg(sender, String.format("Changed %s%s%s to a %snormal%s title.", ChatColor.ITALIC, title.getName(),
                                        ChatColor.RESET, ChatColor.ITALIC, ChatColor.RESET));
                            } else return false;

                        }

                        return true;
                    }
                    return false;

                case "rename":
                    if (args.length == 4) {
                        String type = args[1];
                        String typeValue = Utils.strip(Utils.setColors(args[2]));
                        String newName = Utils.setColors(args[3]);

                        // /titles rename title <title> <newName>
                        if (type.equalsIgnoreCase("title")) {
                            Title title = dataContainer.getTitle(typeValue);
                            if (title != null) {
                                String oldName = title.getName();
                                dataContainer.renameTitle(title, newName);
                                Utils.sendMsg(sender, String.format("Renamed %s%s%s to %s%s%s.", ChatColor.ITALIC, oldName,
                                        ChatColor.RESET, ChatColor.ITALIC, newName, ChatColor.RESET));
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
