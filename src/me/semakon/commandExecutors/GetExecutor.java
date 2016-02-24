package me.semakon.commandExecutors;

import me.semakon.Handlers.GetCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import me.semakon.localStorage.DataContainer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Author:  Martijn
 * Date:    16-2-2016
 */
public class GetExecutor {

    private DataContainer dataContainer;

    public GetExecutor(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        String type = args[0];
        String selector = args[1];

        if (type.equals("get")) {
            switch (selector) {

                // /titles get titles [category] [<category>]
                case "titles":
                    String topLine;
                    List<String> titles;

                    // get titles
                    if (args.length == 2) {
                        topLine = String.format("%sAvailable titles:%s", ChatColor.GOLD, ChatColor.RESET);
                        titles = GetCommands.getTitles(dataContainer);

                    // get titles category <category>
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("category")) {
                        String category = args[3];
                        topLine = String.format("%sAvailable titles in %s:%s", ChatColor.GOLD, category, ChatColor.RESET);
                        titles = GetCommands.getTitlesFromCategory(dataContainer, category);
                        if (titles.isEmpty()) {
                            Utils.sendError(sender, "That category doesn't exist.");
                            return true;
                        }

                    // get titles user <user>
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("user")) {
                        OfflinePlayer user = Utils.getOfflinePlayer(args[3]);
                        if (user == null) {
                            Utils.sendError(sender, "That user doesn't exist.");
                            return true;
                        }
                        topLine = String.format("%s%s's available titles:%s", ChatColor.GOLD, user.getName(), ChatColor.RESET);
                        titles = GetCommands.getMapping(dataContainer, user);
                        if (titles.isEmpty()) {
                            Utils.sendError(sender, "That user doesn't have any titles yet.");
                            return true;
                        }
                    } else return false;

                    if (!titles.isEmpty()) {
                        // send topLine
                        Utils.sendMsg(sender, topLine);

                        // send titles
                        for (String title : titles) {
                            Utils.sendMsg(sender, "- " + title);
                        }
                    } else Utils.sendError(sender, "There are no titles available yet.");
                    return true;

                // /titles get requests
                case "requests":
                    if (args.length == 2) {
                        topLine = String.format("%sPending requests:%s", ChatColor.GOLD, ChatColor.RESET);
                        List<String> requests = GetCommands.getRequests(dataContainer);

                        if (!requests.isEmpty()) {
                            // send topLine
                            Utils.sendMsg(sender, topLine);

                            // send requests
                            for (String request : requests) {
                                Utils.sendMsg(sender, "- " + request);
                            }
                        } else Utils.sendError(sender, "There are no pending requests.");
                        return true;
                    } else return false;

                // /titles get categories
                case "categories":
                    if (args.length == 2) {
                        topLine = String.format("%sCategories:%s", ChatColor.GOLD, ChatColor.RESET);
                        List<String> categories = GetCommands.getCategories(dataContainer);

                        if (!categories.isEmpty()) {
                            // send topLine
                            Utils.sendMsg(sender, topLine);

                            // send categories
                            for (String category : categories) {
                                Utils.sendMsg(sender, "- " + category);
                            }
                        } else Utils.sendError(sender, "There are no categories yet.");
                        return true;
                    } else return false;

                // /titles get request [user] [<user>]
                case "request":
                    String request;

                    // get request
                    if (args.length == 2) {
                        if (player != null) {
                            request = GetCommands.getRequest(dataContainer, player);
                        } else return false;

                    // get request [user] [<user>]
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("user")) {
                        OfflinePlayer user = Utils.getOfflinePlayer(args[3]);
                        // check if player was found.
                        if (user == null) {
                            Utils.sendError(sender, "The server doesn't know that player.");
                            return false;
                        }
                        request = GetCommands.getRequest(dataContainer, user);
                    } else return false;
                    if (request != null) Utils.sendMsg(sender, request);
                    else Utils.sendError(sender, "That player does not have a pending request.");

                    return true;

                // /titles get title <title> [description:category]
                case "title":
                    if (args.length == 4) {

                        String title = Utils.setColors(args[2]).toLowerCase();
                        String typeFromTitle = args[3];
                        String fromTitle;

                        // get title <title> description
                        if (typeFromTitle.equalsIgnoreCase("description")) {
                            fromTitle = GetCommands.getFromTitle(dataContainer, title, Utils.DESC);

                        // get title <title> category
                        } else if (typeFromTitle.equalsIgnoreCase("category")) {
                            fromTitle = GetCommands.getFromTitle(dataContainer, title, Utils.CAT);
                        } else return false;

                        // if title wasn't found send an error message, otherwise send the description.
                        if (fromTitle == null) Utils.sendError(sender, "That title doesn't exist.");
                        else Utils.sendMsg(sender, String.format("%s%s%s of %s%s%s: %s%s%s", ChatColor.GOLD, typeFromTitle, ChatColor.RESET,
                                ChatColor.ITALIC, title, ChatColor.RESET, ChatColor.ITALIC, fromTitle, ChatColor.RESET));
                        return true;

                    } else return false;
            }
        }
        return false;
    }

}
