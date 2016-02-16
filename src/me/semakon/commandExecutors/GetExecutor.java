package me.semakon.commandExecutors;

import me.semakon.Handlers.GetCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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

        String type = args[0];
        String selector = args[1];

        if (type.equals("get")) {
            switch (selector) {

                // /titles get titles category [<category>]
                case "titles":
                    String topLine;
                    List<String> titles;

                    if (args.length == 2) {
                        topLine = String.format("%sAvailable titles:\n%s", ChatColor.GOLD, ChatColor.RESET);
                        titles = GetCommands.getTitles(titlesConfig);
                    } else if (args.length == 4){
                        String category = args[3];
                        if (args[2].equalsIgnoreCase("category")) {
                            topLine = String.format("%sAvailable titles in %s:\n%s", ChatColor.GOLD, category, ChatColor.RESET);
                            titles = GetCommands.getTitlesFromCategory(titlesConfig, category);
                        } else return false;
                    } else return false;

                    if (player == null) Utils.consolePrint(topLine);
                    else player.sendMessage(topLine);

                    for (String title : titles) {
                        if (player == null) Utils.consolePrint(title);
                        else player.sendMessage("- " + title);
                    }

                    return true;

                // /titles get requests
                case "requests":
                    if (args.length == 2) {
                        topLine = String.format("%sPending requests:%s", ChatColor.GOLD, ChatColor.RESET);
                        if (player == null) Utils.consolePrint(topLine);
                        else player.sendMessage(topLine);

                        for (String request : GetCommands.getRequests(requestsConfig)) {
                            if (player == null) Utils.consolePrint(request);
                            else player.sendMessage("- " + request);
                        }
                        return true;
                    } else return false;

                // /titles get categories
                case "categories":
                    if (args.length == 2) {
                        topLine = String.format("%sCategories:%s", ChatColor.GOLD, ChatColor.RESET);
                        if (player == null) Utils.consolePrint(topLine);
                        else player.sendMessage(topLine);

                        for (String category : GetCommands.getCategories(titlesConfig)) {
                            if (player == null) Utils.consolePrint(category);
                            else player.sendMessage("- " + category);
                        }
                        return true;
                    } else return false;

                // /titles get request [user] [<user>]
                case "request":
                    String request;
                    if (args.length == 2) {
                        if (player != null) {
                            request = GetCommands.getRequest(requestsConfig, player);
                        } else return false;
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("user")) {
                        String uuid = Utils.nameToUUID(args[3]);
                        OfflinePlayer user = null;
                        try {
                            UUID uidP = UUID.fromString(uuid);
                            user = Bukkit.getOfflinePlayer(uidP);
                        } catch (IllegalArgumentException e) {
                            Utils.consolePrint(e.getMessage());
                        }
                        if (user == null) {
                            sender.sendMessage("[TitlesPlugin] The server doesn't know that player.");
                            return false;
                        }
                        request = GetCommands.getRequest(requestsConfig, user);
                    } else return false;
                    if (player == null) Utils.consolePrint(request);
                    else player.sendMessage(request);
                    return true;

                // /titles get title <title> [description:category]
                case "title":
                    if (args.length == 4) {
                        String title = args[2].toLowerCase();
                        String fromTitle = args[3];
                        if (fromTitle.equalsIgnoreCase("description")) {
                            fromTitle = GetCommands.getFromTitle(titlesConfig, title, Utils.DESC);
                        } else if (fromTitle.equalsIgnoreCase("category")) {
                            fromTitle = GetCommands.getFromTitle(titlesConfig, title, Utils.CAT);
                        } else return false;
                        if (fromTitle == null) {
                            if (player == null) Utils.consolePrint(ChatColor.RED + "That title doesn't exist.");
                            else Utils.sendError(player, "That title doesn't exist.");
                        } else {
                            if (player == null) Utils.consolePrint(fromTitle);
                            else player.sendMessage(fromTitle);
                            return true;
                        }
                    } else return false;
            }
        }
        return false;
    }

}
