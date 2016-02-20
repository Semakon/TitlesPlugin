package me.semakon.commandExecutors;

import me.semakon.Handlers.GetCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Martijn
 * Date:    16-2-2016
 */
public class GetExecutor {

    private TitlesPlugin plugin;

    public GetExecutor(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");
        ConfigurationSection requestsConfig = plugin.getConfig().getConfigurationSection("Requests");
        ConfigurationSection mappingsConfig = plugin.getConfig().getConfigurationSection("Mappings");

        String type = args[0];
        String selector = args[1];

        if (type.equals("get")) {
            switch (selector) {

                // /titles get titles [category] [<category>]
                case "titles":
                    String topLine;
                    List<String> titles;

                    // If config is empty.
                    if (titlesConfig == null) {
                        Utils.sendError(sender, "There are no titles available yet.");
                        return true;
                    }

                    // get titles
                    if (args.length == 2) {
                        topLine = String.format("%sAvailable titles:%s", ChatColor.GOLD, ChatColor.RESET);
                        titles = GetCommands.getTitles(titlesConfig);

                    // get titles category <category>
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("category")) {
                        String category = args[3];
                        if (args[2].equalsIgnoreCase("category")) {
                            topLine = String.format("%sAvailable titles in %s:%s", ChatColor.GOLD, category, ChatColor.RESET);
                            titles = GetCommands.getTitlesFromCategory(titlesConfig, category);
                        } else return false;

                    // get titles user <user>
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("user")) {
                        OfflinePlayer user = Utils.getOfflinePlayer(args[3]);
                        if (user == null) {
                            Utils.sendError(sender, "That user doesn't exist.");
                            return true;
                        } else if (mappingsConfig == null) {
                            Utils.sendError(sender, "That user doesn't have any titles yet.");
                            return true;
                        }
                        topLine = String.format("%sAvailable titles of %s:%s", ChatColor.GOLD, user.getName(), ChatColor.RESET);
                        titles = GetCommands.getMapping(mappingsConfig, user);
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

                    // If config is empty
                    if (requestsConfig == null) {
                        Utils.sendError(sender, "There are no pending requests.");
                        return true;
                    }

                    if (args.length == 2) {
                        topLine = String.format("%sPending requests:%s", ChatColor.GOLD, ChatColor.RESET);
                        List<String> requests = GetCommands.getRequests(requestsConfig);

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

                    // If config is empty
                    if (titlesConfig == null) {
                        Utils.sendError(sender, "There are no categories yet.");
                        return true;
                    }

                    if (args.length == 2) {
                        topLine = String.format("%sCategories:%s", ChatColor.GOLD, ChatColor.RESET);
                        List<String> categories = GetCommands.getCategories(titlesConfig);

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

                    // If config is empty
                    if (requestsConfig == null) {
                        Utils.sendError(sender, "There are no pending requests.");
                        return true;
                    }

                    String request;

                    // get request
                    if (args.length == 2) {
                        if (player != null) {
                            request = GetCommands.getRequest(requestsConfig, player);
                        } else return false;

                    // get request [user] [<user>]
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("user")) {
                        OfflinePlayer user = Utils.getOfflinePlayer(args[3]);
                        // check if player was found.
                        if (user == null) {
                            Utils.sendError(sender, "The server doesn't know that player.");
                            return false;
                        }
                        request = GetCommands.getRequest(requestsConfig, user);
                    } else return false;
                    if (request != null) Utils.sendMsg(sender, request);
                    else Utils.sendError(sender, "That player does not have a pending request.");

                    return true;

                // /titles get title <title> [description:category]
                case "title":

                    // If config is empty
                    if (titlesConfig == null) {
                        Utils.sendError(sender, "There are no titles available yet.");
                        return true;
                    }

                    if (args.length == 4) {

                        String title = args[2].toLowerCase();
                        String fromTitle = args[3];

                        // get title <title> description
                        if (fromTitle.equalsIgnoreCase("description")) {
                            fromTitle = GetCommands.getFromTitle(titlesConfig, title, Utils.DESC);

                        // get title <title> category
                        } else if (fromTitle.equalsIgnoreCase("category")) {
                            fromTitle = GetCommands.getFromTitle(titlesConfig, title, Utils.CAT);
                        } else return false;

                        // if title wasn't found send an error message, otherwise send the description.
                        if (fromTitle == null) Utils.sendError(sender, "That title doesn't exist.");
                        else Utils.sendMsg(sender, fromTitle);
                        return true;

                    } else return false;
            }
        }
        return false;
    }

}
