package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Mapping;
import me.semakon.localStorage.Title;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class EditTitleCommands {

    /**
     * Constructs a new title with a name, description and category.
     * @param dc Container with all data.
     * @param name Name of the new title.
     * @param description Description of the new title.
     * @param category Category of the new title.
     * @return True if the new title was created successfully.
     */
    public static boolean createTitle(DataContainer dc, String name, String description, String category) {
        Title title = new Title(Utils.strip(Utils.setColors(name.toLowerCase())), Utils.setColors(name), description, category != null ? category : "General");

        // debug
        Utils.consolePrint("Id: " + title.getId());
        Utils.consolePrint("Name: " + title.getName());

        dc.createTitle(title);
        Utils.consolePrint(dc.getTitles().toString());
        return true;
    }

    /**
     * Removes an existing title from the config.
     * @param dc Container with all data.
     * @param id ID of the title to be removed.
     * @return True if the title was removed successfully.
     */
    public static boolean removeTitle(DataContainer dc, String id) {
        Title title = null;
        for (Title t : dc.getTitles()) {
            if (t.getId().equalsIgnoreCase(id)) {
                title = t;
                break;
            }
        }
        if (title == null) return false;
        List<Title> owned = new ArrayList<>();
        for (Mapping mapping : dc.getMappings()) {
            for (Title o : mapping.getOwned()) {
                if (o.equals(title)) {
                    owned = mapping.getOwned();
                    break;
                }
            }
            if (!owned.isEmpty()) {
                owned.remove(title);
                mapping.setOwned(owned);
            }
            if (mapping.getCurrent().equals(title)) mapping.setCurrent(null);
        }
        dc.getTitles().remove(title);
        return true;
    }

    /**
     * Removes an existing category and replaces it with the DEFAULT_CATEGORY.
     * @param plugin This TitlesPlugin.
     * @param category Category that is to be removed.
     * @return True if the category was removed successfully.
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
        if (config != null && config.contains(title.toLowerCase())) {
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
        if (config != null && config.contains(title.toLowerCase())) {
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
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection("Mappings");
        title = title.toLowerCase();

        // If config exists and it contains the title, delete previous title and
        // create new title with same category and description and new name.
        if (titlesConfig != null && titlesConfig.contains(title)) {
            String category = titlesConfig.getString(title + Utils.CAT);
            String description = titlesConfig.getString(title + Utils.DESC);

            titlesConfig.set(title, null);

            titlesConfig.set(name.toLowerCase() + Utils.CAT, category);
            titlesConfig.set(name.toLowerCase() + Utils.DESC, description);
            titlesConfig.set(name.toLowerCase() + Utils.NAME, name);

            if (mapConfig != null) {
                for (String player : mapConfig.getKeys(false)) {
                    if (mapConfig.getString(player + ".Current") != null && mapConfig.getString(player + ".Current").equalsIgnoreCase(title)) {
                        mapConfig.set(player + ".Current", name.toLowerCase());
                    }
                    if (mapConfig.getString(player + ".Owned") != null && mapConfig.getString(player + ".Owned").equalsIgnoreCase(title)) {
                        mapConfig.set(player + ".Owned", name.toLowerCase());
                    }
                }
            }

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
