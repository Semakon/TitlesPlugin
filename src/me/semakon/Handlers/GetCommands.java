package me.semakon.Handlers;

import me.semakon.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class GetCommands {

    /**
     * Creates a list of all titles in a config and returns them.
     * @param config ConfigurationSection that holds the titles.
     * @return List of all titles.
     */
    public static List<String> getTitles(ConfigurationSection config) {
        List<String> titles = config.getKeys(false).stream().map(title ->
                config.getString(title + Utils.NAME)).collect(Collectors.toList());
        if (titles.isEmpty()) titles.add("There are no available titles");
        return titles;
    }

    /**
     * Creates a list of all titles in a specified category in a config.
     * @param config ConfigurationSection that holds the titles.
     * @param category Category where titles are to be listed from.
     * @return List of all titles from a specified category.
     */
    public static List<String> getTitlesFromCategory(ConfigurationSection config, String category) {
        List<String> titles = config.getKeys(false).stream().filter(title ->
                config.getString(title + Utils.CAT).equalsIgnoreCase(category)).map(title ->
                config.getString(title + Utils.NAME)).collect(Collectors.toList());
        if (titles.isEmpty()) titles.add("That category doesn't exist.");
        return titles;
    }

    /**
     * Returns a list of all categories in a config.
     * @param config ConfigurationSection that holds the categories.
     * @return List of all categories.
     */
    public static List<String> getCategories(ConfigurationSection config) {
        List<String> categories = new ArrayList<>();
        for (String title : config.getKeys(false)) {
            String cat = config.getConfigurationSection(title).getString(Utils.CAT);
            if (!categories.contains(cat)) {
                categories.add(cat);
            }
        }
        if (categories.isEmpty()) categories.add("There are no categories.");
        return categories;
    }

    /**
     * Returns a list of all pending requests in a config.
     * @param config ConfigurationSection that holds the pending requests.
     * @return List of all pending requests.
     */
    public static List<String> getRequests(ConfigurationSection config) {
        List<String> requests = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            String player = config.getConfigurationSection(key).getString("Name");
            String title = config.getConfigurationSection(key).getString("Title");
            requests.add(player + ": " + title);
        }
        if (requests.isEmpty()) requests.add("There are no pending requests.");
        return requests;
    }

    /**
     * Returns the key's value from a title in a config.
     * @param config ConfigurationSection that holds the title.
     * @param title Title where key's value is to be returned from.
     * @param key Key with value.
     * @return A specified key's value from a specified title.
     */
    public static String getFromTitle(ConfigurationSection config, String title, String key) {
        if (config.contains(title)) {
            Utils.consolePrint(config.getString(title + key));
            return config.getString(title + key);
        } else return null;
    }

    /**
     * Returns the pending request of the player from a config.
     * @param config ConfigurationSection that holds the requests.
     * @param player Player whose request is queried.
     * @return The pending request of a player.
     */
    public static String getRequest(ConfigurationSection config, Player player) {
        String uuid = player.getUniqueId().toString();
        for (String key : config.getKeys(false)) {
            if (uuid.equalsIgnoreCase(key)) {
                String title = config.getString(key + ".Title");
                String comments = config.getString(key + ".Comments");
                return title + " - " + comments;
            }
        }
        return "That player doesn't have a pending request.";
    }

}
