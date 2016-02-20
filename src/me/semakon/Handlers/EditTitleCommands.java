package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class EditTitleCommands {

    /**
     * Creates a new title with a name, description and category. It is then saved to the config.
     * @param plugin This TitlesPlugin
     * @param title Name of the new title.
     * @param description Description of the new title.
     * @param category Category of the new title.
     * @return True if the new title was created successfully.
     */
    public static boolean createTitle(TitlesPlugin plugin, String title, String description, String category) {
        ConfigurationSection config = plugin.getConfig();

        // If configurationSection Titles doesn't exist or it doesn't contain this title yet, add it to Titles.
        if (config.getConfigurationSection("Titles") == null || !config.contains(title.toLowerCase())) {
            config.set(Utils.TITLES + title.toLowerCase() + Utils.NAME, title);
            config.set(Utils.TITLES + title.toLowerCase() + Utils.DESC, description);
            config.set(Utils.TITLES + title.toLowerCase() + Utils.CAT, category);
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

        // If config exists and it contains the title, remove it.
        if (config != null && config.contains(title.toLowerCase())) {
            config.set(title.toLowerCase(), null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Removes a category and turns all
     * @param plugin
     * @param category
     * @return
     */
    public static boolean removeCategory(TitlesPlugin plugin, String category) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");

        // If config exists and the category exists.
        if (config != null) {
            for (String key : config.getKeys(false)) {
                if (config.getString(key + Utils.CAT).equalsIgnoreCase(category)) {
                    config.set(key + Utils.CAT, Utils.DEFAULT_CATEGORY);
                }
            }
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

        // If config exists and it contains the title, change the description.
        if (config != null && config.contains(title)) {
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

        // If config exists and it contains the title, change the category.
        if (config != null && config.contains(title)) {
            config.set(title + Utils.CAT, category.toLowerCase());
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Gives a title a new name.
     * @param plugin This TitlesPlugin.
     * @param title The title to be renamed.
     * @param name The new name of the title.
     * @return True if the title was renamed successfully.
     */
    public static boolean renameTitle(TitlesPlugin plugin, String title, String name) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");

        // If config exists and it contains the title, create new title with same category and description,
        // and the correct name. Also, delete previous title.
        if (config != null && config.contains(title)) {
            config.set(name.toLowerCase() + Utils.CAT, config.getString(title + Utils.CAT));
            config.set(name.toLowerCase() + Utils.DESC, config.getString(title + Utils.DESC));
            config.set(name.toLowerCase() + Utils.NAME, name);
            config.set(title, null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Gives a category a new name and changes all titles with that category to fit.
     * @param plugin This TitlesPlugin.
     * @param category Category that is to be renamed.
     * @param name New name of the category.
     * @return True if the category was renamed successfully.
     */
    public static boolean renameCategory(TitlesPlugin plugin, String category, String name) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");

        if (config != null) {
            for (String key : config.getKeys(false)) {
                if (config.getString(key + Utils.CAT).equalsIgnoreCase(category)) {
                    config.set(key + Utils.CAT, name);
                }
            }
            return true;
        }
        return false;
    }

}