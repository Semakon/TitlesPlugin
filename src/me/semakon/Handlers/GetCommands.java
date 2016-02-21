package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<String> titles = new ArrayList<>();
        for (String title : config.getKeys(false)) {
            titles.add(config.getString(title + Utils.NAME));
        }
        return titles;
    }

    /**
     * Creates a list of all titles in a specified category in a config.
     * @param config ConfigurationSection that holds the titles.
     * @param category Category where titles are to be listed from.
     * @return List of all titles from a specified category.
     */
    public static List<String> getTitlesFromCategory(ConfigurationSection config, String category) {
        List<String> titles = new ArrayList<>();
        for (String title : config.getKeys(false)) {
            if (config.getString(title + Utils.CAT).equalsIgnoreCase(category)) {
                config.getString(title + Utils.NAME);
                titles.add(title);
            }
        }
        return titles;
    }

    /**
     * Creates a list of all titles a player owns. Returns a list with an error message if the player doesn't have any titles.
     * @param plugin This TitlesPlugin used to get the configs.
     * @param player Player whose owned titles are queried.
     * @return List of all owned titles of a player.
     */
    public static List<String> getMapping(TitlesPlugin plugin, OfflinePlayer player) {
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection("Mappings");
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");
        String uuid = player.getUniqueId().toString();
        List<String> titles = new ArrayList<>();

        if (mapConfig == null || titlesConfig == null) return titles;
        if (mapConfig.contains(uuid)) {
            for (String owned : mapConfig.getStringList(uuid + ".Owned")) {
                for (String title : titlesConfig.getKeys(false)) {
                    if (owned.equalsIgnoreCase(title)) {
                        titles.add(titlesConfig.getString(title + ".Name"));
                    }
                }
            }
        }
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
            String cat = config.getConfigurationSection(title.toLowerCase()).getString(Utils.CAT);
            if (!categories.contains(cat)) {
                categories.add(cat);
            }
        }
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
            UUID uuid = UUID.fromString(key);
            String title = config.getConfigurationSection(key).getString("Title");
            requests.add(Bukkit.getOfflinePlayer(uuid).getName() + ": " + title);
        }
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
    public static String getRequest(ConfigurationSection config, OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        for (String key : config.getKeys(false)) {
            if (uuid.equalsIgnoreCase(key)) {
                String title = config.getString(key + ".Title");
                return player.getName() + ": " + title;
            }
        }
        return null;
    }

}
