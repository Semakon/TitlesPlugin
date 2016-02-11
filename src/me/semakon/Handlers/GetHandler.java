package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class GetHandler {

    public static List<String> getTitles(TitlesPlugin plugin) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        System.out.println("config: " + config);
        return config.getKeys(false).stream().map(title ->
                config.getString(title + Utils.NAME)).collect(Collectors.toList());
    }

    public static List<String> getTitlesFromCategory(TitlesPlugin plugin, String category) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        return config.getKeys(false).stream().filter(title ->
                config.getString(title + Utils.CAT).equalsIgnoreCase(category)).map(title ->
                config.getString(title + Utils.NAME)).collect(Collectors.toList());
    }

    public static List<String> getCategories(TitlesPlugin plugin) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        List<String> categories = new ArrayList<>();
        for (String title : config.getKeys(false)) {
            String cat = config.getConfigurationSection(title).getString(Utils.CAT);
            if (!categories.contains(cat)) {
                categories.add(cat);
            }
        }
        return categories;
    }

    public static boolean getDescription(TitlesPlugin plugin, String title) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        return false;
    }

    public static boolean getCategory(TitlesPlugin plugin, String title) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        return false;
    }

}
