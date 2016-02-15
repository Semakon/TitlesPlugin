package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class EditCommands {

    /**
     * Creates a new title with a name, description and category. It is then saved to the config.
     * @param plugin This TitlesPlugin
     * @param title Name of the new title.
     * @param description Description of the new title.
     * @param category Category of the new title.
     * @return True if the new title was created successfully.
     */
    public static boolean createTitle(TitlesPlugin plugin, String title, String description, String category) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (!config.contains(title.toLowerCase())) {
            config.set(title.toLowerCase() + Utils.NAME, title);
            config.set(title.toLowerCase() + Utils.DESC, description);
            config.set(title.toLowerCase() + Utils.CAT, category);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Removes an existing title from the config.
     * @param plugin This TitlesPlugin.
     * @param title The title to be removed.
     * @return True if the title was removed successfully.
     */
    public static boolean removeTitle(TitlesPlugin plugin, String title) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (config.contains(title.toLowerCase())) {
            config.set(title.toLowerCase(), null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Edits the description of an existing title and saves it to the config.
     * @param plugin This TitlesPlugin.
     * @param title The title that is edited.
     * @param description The new description.
     * @return True if the description was edited successfully.
     */
    public static boolean editDescription(TitlesPlugin plugin, String title, String description) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (config.contains(title)) {
            config.set(title + Utils.DESC, description);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Edits the category of an existing title and saves it to the config. If the category doesn't exist, it will be created.
     * @param plugin This TitlesPlugin.
     * @param title The title that is edited.
     * @param category The new category.
     * @return True if the category was edited successfully.
     */
    public static boolean editCategory(TitlesPlugin plugin, String title, String category) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (config.contains(title)) {
            config.set(title + Utils.CAT, category);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

}
