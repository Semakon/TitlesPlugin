package me.semakon.Handlers;

import me.semakon.Utils;
import org.bukkit.configuration.ConfigurationSection;

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
     * @param config Config that holds the titles.
     * @return List of all titles.
     */
    public static List<String> getTitles(ConfigurationSection config) {
        System.out.println("config: " + config);
        return config.getKeys(false).stream().map(title ->
                config.getString(title + Utils.NAME)).collect(Collectors.toList());
    }

    /**
     * Creates a list of all titles in a specified category in a config.
     * @param config Config that holds the titles.
     * @param category Category where titles are to be listed from.
     * @return List of all titles from a specified category.
     */
    public static List<String> getTitlesFromCategory(ConfigurationSection config, String category) {
        return config.getKeys(false).stream().filter(title ->
                config.getString(title + Utils.CAT).equalsIgnoreCase(category)).map(title ->
                config.getString(title + Utils.NAME)).collect(Collectors.toList());
    }

    /**
     * Returns a list of all categories in a config.
     * @param config Config that holds the categories.
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
        return categories;
    }

    /**
     * Returns key's value from a title in a config.
     * @param config Config that holds the title.
     * @param title Title where key's value is to be returned from.
     * @param key Key with value.
     * @return A specified key's value from a specified title.
     */
    public static String getFromTitle(ConfigurationSection config, String title, String key) {
        if (config.contains(config.getString(title))) {
            return config.getString(title + key);
        } else return null;
    }

}
