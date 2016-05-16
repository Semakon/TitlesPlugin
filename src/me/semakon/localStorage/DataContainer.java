package me.semakon.localStorage;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import me.semakon.localStorage.Exceptions.CannotRemoveDefaultCategoryException;
import me.semakon.localStorage.Exceptions.InvalidCategoryRuntimeException;
import me.semakon.localStorage.Exceptions.InvalidTitleRuntimeException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class DataContainer {

    private TitlesPlugin plugin;

    private List<Category> categories;
    private List<Title> titles;
    private List<UserData> userData;

    private Category defaultCategory;

    /**
     * Initiates all values and creates the default category.
     * @param plugin The TitlesPlugin this dataContainer belongs to.
     */
    public DataContainer(TitlesPlugin plugin) {
        this.plugin = plugin;

        categories = new ArrayList<>();
        titles = new ArrayList<>();
        userData = new ArrayList<>();

        defaultCategory = new Category(Utils.DEFAULT_CATEGORY.toLowerCase(), Utils.DEFAULT_CATEGORY, "This is the default category.");
    }

    //-------------------------------------------------------------- Categories ------------------------------------------------------------------------

    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Gets the category with id and returns it.
     * @param id The category's ID.
     * @return The category with ID id or null if it doesn't exist.
     */
    public Category getCategory(String id) {
        for (Category cat : categories) {
            if (cat.getId().equalsIgnoreCase(id)) {
                return cat;
            }
        }
        return null;
    }

    /**
     * Gets the default category.
     * @return The default category.
     */
    public Category getDefaultCategory() {
        return defaultCategory;
    }

    /**
     * Add a new category to the categories list.
     * @param category Category that is added.
     */
    public void addCategory(Category category) {
        categories.add(category);
    }

    /**
     * Rename a category to newName and the ID to newName without the colors and in lower case.
     * @param category Category that has its name changed.
     * @param newName The new name of the category.
     */
    public void renameCategory(Category category, String newName) {
        category.setId(Utils.strip(Utils.setColors(newName)).toLowerCase());
        category.setName(newName);
    }

    /**
     * Edit a category's description.
     * @param category Category that has its description edited.
     * @param newDescription The new description.
     */
    public void editCategoryDescription(Category category, String newDescription) {
        category.setDescription(newDescription);
    }

    /**
     * Removes a category from the categories list and changes all titles with that category
     * to have the default category instead.
     * @param category Category that is removed.
     * @throws CannotRemoveDefaultCategoryException If the category is the default category.
     */
    public void removeCategory(Category category) throws CannotRemoveDefaultCategoryException {
        if (category.equals(defaultCategory)) throw new CannotRemoveDefaultCategoryException();
        for (Title title : titles) {
            if (title.getCategory().equals(category)) title.setCategory(defaultCategory);
        }
        categories.remove(category);
    }

    //-------------------------------------------------------------- Titles ------------------------------------------------------------------------

    public List<Title> getTitles() {
        return titles;
    }

    /**
     * Gets the title with id and returns it.
     * @param id The title's ID.
     * @return The title with ID id or null if it doesn't exist.
     */
    public Title getTitle(String id) {
        for (Title title : titles) {
            if (title.getId().equalsIgnoreCase(id)) {
                return title;
            }
        }
        return null;
    }

    /**
     * Adds a title to the titles list.
     * @param title Title that is added.
     */
    public void addTitle(Title title) {
        titles.add(title);
    }

    /**
     * Removes a title from all userData, requests and finally the titles list.
     * @param title Title that is removed.
     */
    public void removeTitle(Title title) {
        // remove title from userData
        for (UserData userData : this.userData) {

            // remove title from list of unlocked titles
            if (userData.getUnlocked().contains(title)) userData.getUnlocked().remove(title);

            // remove title from current
            if (userData.getCurrent().equals(title)) userData.setCurrent(null);
        }

        // remove title from requests
        List<Request> requests = new ArrayList<>();
        for (UserData ud : userData) {
            if (ud.getRequest().getTitle().equals(title)) requests.add(ud.getRequest());
        }
        for (Request request : requests) removeRequest(request);

        // remove title from titles
        titles.remove(title);
    }

    /**
     * Changes the description of a title to newDescription.
     * @param title Title whose description is changed.
     * @param newDescription New description of the title.
     */
    public void editTitleDescription(Title title, String newDescription) {
        title.setDescription(newDescription);
    }

    /**
     * Changes the category of a title to newCategory.
     * @param title Title whose category is changed.
     * @param newCategory New category of the title.
     */
    public void editTitleCategory(Title title, Category newCategory) {
        title.setCategory(newCategory);
    }

    /**
     * Sets the boolean unique of a title to parameter unique.
     * @param title Title that has its unique changed.
     * @param unique New value of unique.
     */
    public void editTitleUnique(Title title, boolean unique) {
        title.setUnique(unique);
    }

    /**
     * Changes the name of a title to newName and the ID to newName without the colors and in lower case.
     * @param title Title that is renamed.
     * @param newName New name of the title.
     */
    public void renameTitle(Title title, String newName) {
        title.setId(Utils.strip(Utils.setColors(newName)).toLowerCase());
        title.setName(newName);
    }

    /**
     * Gets a list of all titles from a certain category.
     * @param category Category the titles are from.
     * @return A list of titles from a category.
     */
    public List<Title> getTitlesFromCategory(Category category) {
        List<Title> titles = new ArrayList<>();
        for (Title title : this.titles) {
            if (title.getCategory().equals(category)) titles.add(title);
        }
        return titles;
    }

    //-------------------------------------------------------------- Requests ------------------------------------------------------------------------

    public Request getRequest(OfflinePlayer player) {
        for (UserData ud : userData) {
            if (ud.getUuid().equals(player.getUniqueId())) return ud.getRequest();
        }
        return null;
    }

    /**
     * Gets the title that a player requested.
     * @param player The player that requested the title.
     * @return Title that is requested, or if it doesn't exist null.
     */
    public Title getRequestedTitle(OfflinePlayer player) {
        for (UserData ud : userData) {
            if (ud.getUuid().equals(player.getUniqueId())) return ud.getRequest().getTitle();
        }
        return null;
    }

    /**
     * Gets the location of a request of a player.
     * @param player The player that made the request.
     * @return The location of a request of a player, or null if the request/location doesn't exist.
     */
    public Location getRequestLocation(OfflinePlayer player) {
        for (UserData ud : userData) {
            if (ud.getUuid().equals(player.getUniqueId())) return ud.getRequest().getLocation();
        }
        return null;
    }

    /**
     * Removes a request from the requests list.
     * @param request Request that is removed.
     */
    public void removeRequest(Request request) {
        for (UserData ud : userData) {
            if (ud.getRequest().equals(request)) ud.setRequest(null);
        }
    }

    //-------------------------------------------------------------- UserData ------------------------------------------------------------------------

    public List<UserData> getUserData() {
        return userData;
    }

    /**
     * Sets the current title of a player to <code>title</code> if the player owns that title.
     * @param player The player whose title is set.
     * @param title The title that is set.
     */
    public void setCurrentTitle(OfflinePlayer player, Title title) {
        for (UserData userData : this.userData) {
            if (userData.getUuid().equals(player.getUniqueId()) && getUnlockedTitles(player).contains(title)) {
                userData.setCurrent(title);
            }
        }
    }

    /**
     * Gets the current title of a player.
     * @param player The title belongs to this player.
     * @return Current title of player, or null if it doesn't exist.
     */
    public Title getCurrentTitle(OfflinePlayer player) {
        for (UserData userData : this.userData) {
            if (userData.getUuid().equals(player.getUniqueId())) return userData.getCurrent();
        }
        return null;
    }

    /**
     * Gets a list of all titles unlocked by a player.
     * @param player The player that owns the titles.
     * @return A list of all titles unlocked by a player.
     */
    public List<Title> getUnlockedTitles(OfflinePlayer player) {
        List<Title> titles = new ArrayList<>();
        for (UserData userData : this.userData) {
            if (userData.getUuid().equals(player.getUniqueId())) {
                titles.addAll(userData.getUnlocked());
            }
        }
        return titles;
    }

    /**
     * Removes a title from a player's unlocked list.
     * @param player The player that has their title removed.
     * @param title The title that is removed.
     */
    public void removeFromUnlockedTitles(OfflinePlayer player, Title title) {
        for (UserData userData : this.userData) {
            if (userData.getUuid().equals(player.getUniqueId())) {
                List<Title> unlocked = userData.getUnlocked();
                unlocked.remove(title);
                userData.setUnlocked(unlocked);
                if (title.isUnique()) title.setUniqueTo(null);
                return;
            }
        }
    }

    /**
     * Adds a title to a player's unlocked list.
     * @param player The player that gets the title.
     * @param title The title that the player gets.
     */
    public void addToUnlockedTitles(OfflinePlayer player, Title title) {
        for (UserData userData : this.userData) {
            if (userData.getUuid().equals(player.getUniqueId())) {
                List<Title> unlocked = userData.getUnlocked();
                unlocked.add(title);
                userData.setUnlocked(unlocked);
                if (title.isUnique()) title.setUniqueTo(player.getUniqueId());
                return;
            }
        }
        List<Title> unlocked = new ArrayList<>();
        unlocked.add(title);
        UserData userData = new UserData(player.getUniqueId(), unlocked, null);
        this.userData.add(userData);
    }

    //-------------------------------------------------------------- Loading ------------------------------------------------------------------------

    /**
     * Clears all lists and reloads all storage from the yaml files again.
     */
    public void reloadAllData() {
        categories.clear();
        titles.clear();
        userData.clear();
        loadStorage();
    }

    /**
     * Clears categories and titles lists and reloads them from the config file.
     */
    public void reloadTitleData() {
        categories.clear();
        titles.clear();
        try {
            loadCategories();
            loadTitles();
        } catch (InvalidTitleRuntimeException e) {
            Utils.consolePrint("Error while trying to load data from config.yml");
            Utils.save = false;
            plugin.getPluginLoader().disablePlugin(plugin);
            e.printStackTrace();
        }
    }

    /**
     * Clears userData list and reloads it from the config file.
     */
    public void reloadUserData() {
        userData.clear();
        try {
            loadUserData();
        } catch (InvalidTitleRuntimeException e) {
            Utils.consolePrint("Error while trying to load data from config.yml");
            Utils.save = false;
            plugin.getPluginLoader().disablePlugin(plugin);
            e.printStackTrace();
        }
    }

    /**
     * Loads all data from the yaml file to the local storage.
     */
    public void loadStorage() {
        categories.add(defaultCategory);
        try {
            loadSettings();
            loadCategories();
            loadTitles();
            loadUserData();
        } catch (InvalidTitleRuntimeException | InvalidCategoryRuntimeException e) {
            Utils.consolePrint("Error while trying to load data from config.yml");
            Utils.save = false;
            plugin.getPluginLoader().disablePlugin(plugin);
            e.printStackTrace();
        }

        // debug
        if (Settings.isDebuggingOn()) {
            for (Category c : categories) Utils.consolePrint("Category: " + c.getName());
            for (Title t : titles) Utils.consolePrint("Title: " + t.getName());
            for (UserData m : userData) Utils.consolePrint("UserData: " + Bukkit.getOfflinePlayer(m.getUuid()).getName()
                    + ": " + m.getCurrent());
        }
    }

    /**
     * Loads the settings from the yaml file.
     */
    public void loadSettings() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Settings");
        if (config != null) {
            Settings.setAutoSave(config.getBoolean(".AutoSave"));
            Settings.setDebugging(config.getBoolean(".Debugging"));
            Settings.setDonatorSuffixes(config.getBoolean(".DonatorSuffixes"));
        }
    }

    /**
     * Loads the data of the categories from the yaml file.
     */
    private void loadCategories() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Categories");
        if (config != null) {
            for (String key : config.getKeys(false)) {
                if (key.equalsIgnoreCase(defaultCategory.getId())) {
                    defaultCategory.setName(config.getString(key + ".Name"));
                    defaultCategory.setDescription(config.getString(key + ".Description"));
                    continue;
                }
                // key is identifier.
                String name = config.getString(key + ".Name");
                String description = config.getString(key + ".Description");
                categories.add(new Category(key, name, description));
            }
        }
    }

    /**
     * Loads the data of the titles from the yaml file.
     * @throws InvalidTitleRuntimeException When a title is invalid in any way.
     */
    private void loadTitles() throws InvalidCategoryRuntimeException {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (config != null) {
            for (String key : config.getKeys(false)) {
                // key is identifier.
                String name = config.getString(key + ".Name");
                String description = config.getString(key + ".Description");
                boolean unique = config.getBoolean(key + ".Unique");

                // load UniqueTo
                String uuid = config.getString(key + ".UniqueTo");
                UUID uniqueTo = null;
                if (uuid != null) uniqueTo = UUID.fromString(uuid);

                // load category
                Category category = null;
                for (Category c : categories) {
                    if (c.getId().equalsIgnoreCase(config.getString(key + ".Category"))) {
                        category = c;
                        break;
                    }
                }
                if (category == null) throw new InvalidCategoryRuntimeException();

                titles.add(new Title(key, name, description, category, unique, uniqueTo));
            }
        }
    }

    /**
     * Loads the UserData from the yaml file.
     * @throws InvalidTitleRuntimeException When a title is invalid in any way.
     */
    private void loadUserData() throws InvalidTitleRuntimeException {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Userdata");
        if (config != null) {
            for (String key : config.getKeys(false)) {

                // load uuid
                UUID uuid = UUID.fromString(key);

                // load unlocked
                List<Title> unlocked = new ArrayList<>();
                List<String> configUnlocked = config.getStringList(key + ".Unlocked");
                for (Title t : titles) {
                    for (String name : configUnlocked) {
                        if (t.getId().equalsIgnoreCase(name)) unlocked.add(t);
                    }
                }

                // load current
                Title current = null;
                for (Title t : titles) {
                    if (t.getId().equalsIgnoreCase(config.getString(key + ".Current"))) {
                        current = t;
                        break;
                    }
                }

                // load request title
                Title title = null;
                for (Title t : titles) {
                    if (t.getId().equalsIgnoreCase(config.getString(key + ".Request.Title"))) {
                        title = t;
                        break;
                    }
                }
                if (title == null) throw new InvalidTitleRuntimeException();

                // load request status
                RequestStatus status = RequestStatus.fromString(config.getString(key + ".Request.Status"));

                // load request location
                World world = Bukkit.getWorld(config.getString(key + ".Request.Location.World"));
                double x = config.getDouble(uuid + ".Request.Location.X");
                double y = config.getDouble(uuid + ".Request.Location.Y");
                double z = config.getDouble(uuid + ".Request.Location.Z");
                Location loc = new Location(world, x, y, z);

                // add request and userData to lists
                Request request = new Request(uuid, title, loc, status);

                userData.add(new UserData(uuid, unlocked, current, request));
            }
        }
    }

    //-------------------------------------------------------------- Saving ------------------------------------------------------------------------

    /**
     * Saves all local data to the yaml file.
     */
    public void saveStorage() {
        saveSettings(plugin.getConfig());
        saveCategories(plugin.getConfig());
        saveTitles(plugin.getConfig());
        saveUserData(plugin.getConfig());
        plugin.saveConfig();
    }

    /**
     * Creates a backup file of the current data, NOT the config files.
     * If there already exists a backup file, a new file is created with a number higher than the previous.
     */
    public void backup() {
        //TODO: create a file to save the backup to.
//        String filename = "backup.yml";
//        File backup = new File(plugin.getDataFolder() + Utils.BACKUP_FOLDER, filename);

        //TODO: construct a customConfig from the file.
        saveSettings(plugin.getConfig());
        saveCategories(plugin.getConfig());
        saveTitles(plugin.getConfig());
        saveUserData(plugin.getConfig());
    }

    /**
     * Saves the settings to the yaml file.
     */
    private void saveSettings(Configuration config) {
        config.set("AutoSave", Settings.isAutoSaveOn());
        config.set("Debugging", Settings.isDebuggingOn());
        config.set("DonatorSuffixes", Settings.getDonatorSuffixes());
    }

    /**
     * Saves all local data of the categories to the yaml file.
     */
    private void saveCategories(Configuration config) {
        config.set("Categories", null);
        for (Category category : categories) {
            config.set(Utils.CATEGORIES + category.getId() + ".Name", category.getName());
            config.set(Utils.CATEGORIES + category.getId() + ".Description", category.getDescription());
        }
    }

    /**
     * Saves all local data of the titles to the yaml file.
     */
    private void saveTitles(Configuration config) {
        config.set("Titles", null);
        for (Title title : titles) {
            String id = title.getId();
            config.set(Utils.TITLES + id + ".Name", title.getName());
            config.set(Utils.TITLES + id + ".Description", title.getDescription());
            config.set(Utils.TITLES + id + ".Category", title.getCategory().getId());
            config.set(Utils.TITLES + id + ".Unique", title.isUnique());
            config.set(Utils.TITLES + id + ".UniqueTo", title.getUniqueTo());
        }
    }

    /**
     * Saves all local user data to the yaml file.
     */
    private void saveUserData(Configuration cfg) {
        ConfigurationSection config = cfg.getConfigurationSection("Userdata");
        for (UserData userData : this.userData) {

            // string of the key
            String uuid = userData.getUuid().toString();

            // save unlocked titles
            List<String> unlocked = new ArrayList<>();
            for (Title title : userData.getUnlocked()) {
                unlocked.add(title.getId());
            }
            config.set(uuid + ".Unlocked", unlocked);

            // save current title
            config.set(uuid + ".Current", userData.getCurrent() != null ? userData.getCurrent().getId() : null);

            // save request title and status
            Request request = userData.getRequest();
            config.set(uuid + ".Request.Title", request.getTitle().getId());
            config.set(uuid + ".Request.Status", request.getStatus().toString());

            // save request location
            Location loc = request.getLocation();
            config.set(uuid + ".Request.Location.World", loc.getWorld().getName());
            config.set(uuid + ".Request.Location.X", loc.getX());
            config.set(uuid + ".Request.Location.Y", loc.getY());
            config.set(uuid + ".Request.Location.Z", loc.getZ());
        }
    }

}
