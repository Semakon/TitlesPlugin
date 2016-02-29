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
    private List<Request> requests;
    private List<Mapping> mappings;

    private Category defaultCategory;

    /**
     * Initiates all values and creates the default category.
     * @param plugin The TitlesPlugin this dataContainer belongs to.
     */
    public DataContainer(TitlesPlugin plugin) {
        this.plugin = plugin;

        categories = new ArrayList<>();
        titles = new ArrayList<>();
        requests = new ArrayList<>();
        mappings = new ArrayList<>();

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
     * Removes a title from all mappings, requests and finally the titles list.
     * @param title Title that is removed.
     */
    public void removeTitle(Title title) {
        // remove title from mappings
        for (Mapping mapping : mappings) {
            List<Title> owned = new ArrayList<>();
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

        // remove title from requests
        List<Request> requests = new ArrayList<>();
        for (Request request : this.requests) {
            if (request.getTitle().equals(title)) requests.add(request);
        }
        for (Request request : requests) {
            removeRequest(request);
        }

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

    public List<Request> getRequests() {
        return requests;
    }

    public Request getRequest(OfflinePlayer player) {
        for (Request request : requests) {
            if (request.getUuid().equals(player.getUniqueId())) return request;
        }
        return null;
    }

    /**
     * Gets the title that a player requested.
     * @param player The player that requested the title.
     * @return Title that is requested, or if it doesn't exist null.
     */
    public Title getRequestedTitle(OfflinePlayer player) {
        for (Request request : requests) {
            if (request.getUuid().equals(player.getUniqueId())) return request.getTitle();
        }
        return null;
    }

    /**
     * Gets the location of a request of a player.
     * @param player The player that made the request.
     * @return The location of a request of a player, or null if the request/location doesn't exist.
     */
    public Location getRequestLocation(OfflinePlayer player) {
        for (Request request : requests) {
            if (request.getUuid().equals(player.getUniqueId())) return request.getLocation();
        }
        return null;
    }

    /**
     * Removes a request from the requests list.
     * @param request Request that is removed.
     */
    public void removeRequest(Request request) {
        requests.remove(request);
    }

    //-------------------------------------------------------------- Mappings ------------------------------------------------------------------------

    public List<Mapping> getMappings() {
        return mappings;
    }

    /**
     * Sets the current title of a player to <code>title</code> if the player owns that title.
     * @param player The player whose title is set.
     * @param title The title that is set.
     */
    public void setCurrentTitle(OfflinePlayer player, Title title) {
        for (Mapping mapping : mappings) {
            if (mapping.getUuid().equals(player.getUniqueId()) && getOwnedTitles(player).contains(title)) {
                mapping.setCurrent(title);
            }
        }
    }

    /**
     * Gets the current title of a player.
     * @param player The title belongs to this player.
     * @return Current title of player, or null if it doesn't exist.
     */
    public Title getCurrentTitle(OfflinePlayer player) {
        for (Mapping mapping : mappings) {
            if (mapping.getUuid().equals(player.getUniqueId())) return mapping.getCurrent();
        }
        return null;
    }

    /**
     * Gets a list of all titles owned by a player.
     * @param player The player that owns the titles.
     * @return A list of all titles owned by a player.
     */
    public List<Title> getOwnedTitles(OfflinePlayer player) {
        List<Title> titles = new ArrayList<>();
        for (Mapping mapping : mappings) {
            if (mapping.getUuid().equals(player.getUniqueId())) {
                titles.addAll(mapping.getOwned());
            }
        }
        return titles;
    }

    /**
     * Removes a title from a player's owned list.
     * @param player The player that has their title removed.
     * @param title The title that is removed.
     */
    public void removeFromOwnedTitles(OfflinePlayer player, Title title) {
        for (Mapping mapping : mappings) {
            if (mapping.getUuid().equals(player.getUniqueId())) {
                List<Title> owned = mapping.getOwned();
                owned.remove(title);
                mapping.setOwned(owned);
                if (title.isUnique()) title.setUniqueTo(null);
                return;
            }
        }
    }

    /**
     * Adds a title to a player's owned list.
     * @param player The player that gets the title.
     * @param title The title that the player gets.
     */
    public void addToOwnedTitles(OfflinePlayer player, Title title) {
        for (Mapping mapping : mappings) {
            if (mapping.getUuid().equals(player.getUniqueId())) {
                List<Title> owned = mapping.getOwned();
                owned.add(title);
                mapping.setOwned(owned);
                if (title.isUnique()) title.setUniqueTo(player.getUniqueId());
                return;
            }
        }
        List<Title> owned = new ArrayList<>();
        owned.add(title);
        Mapping mapping = new Mapping(player.getUniqueId(), owned, null);
        mappings.add(mapping);
    }

    //-------------------------------------------------------------- Loading ------------------------------------------------------------------------

    /**
     * Clears all lists, adds the default category to the categories list and
     * reloads all storage from the yaml files again.
     */
    public void reload() {
        categories.clear();
        titles.clear();
        requests.clear();
        mappings.clear();
        loadStorage();
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
            loadMappings();
            loadRequests();
        } catch (InvalidTitleRuntimeException | InvalidCategoryRuntimeException e) {
            Utils.consolePrint("Problem with loading data from config.yml");
            Utils.save = false;
            plugin.getPluginLoader().disablePlugin(plugin);
            e.printStackTrace();
        }

        // debug
        if (Settings.isDebugging()) {
            for (Category c : categories) Utils.consolePrint("Category: " + c.getName());
            for (Title t : titles) Utils.consolePrint("Title: " + t.getName());
            for (Request r : requests) Utils.consolePrint("Request: " + Bukkit.getOfflinePlayer(r.getUuid()).getName() + ": " + r.getTitle().getName() + ", " + r.getStatus());
            for (Mapping m : mappings) Utils.consolePrint("Mapping: " + Bukkit.getOfflinePlayer(m.getUuid()).getName() + ": " + m.getCurrent());
        }
    }

    private void loadSettings() {
        //TODO: Implement
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
     */
    private void loadTitles() throws InvalidCategoryRuntimeException {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (config != null) {
            for (String key : config.getKeys(false)) {
                // key is identifier.
                String name = config.getString(key + ".Name");
                String description = config.getString(key + ".Description");
                boolean unique = config.getBoolean(key + ".Unique");

                // UniqueTo
                String uuid = config.getString(key + ".UniqueTo");
                UUID uniqueTo = null;
                if (uuid != null) uniqueTo = UUID.fromString(uuid);

                // category
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
     * Loads the data of the requests from the yaml file.
     */
    private void loadRequests() throws InvalidTitleRuntimeException {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");
        if (config != null) {
            for (String key : config.getKeys(false)) {

                // uuid
                UUID uuid = UUID.fromString(key);

                // titles
                Title title = null;
                for (Title t : titles) {
                    if (t.getId().equalsIgnoreCase(config.getString(key + ".Title"))) {
                        title = t;
                        break;
                    }
                }
                if (title == null) throw new InvalidTitleRuntimeException();

                // status
                RequestStatus status = RequestStatus.fromString(config.getString(key + ".Status"));

                // location
                World world = Bukkit.getWorld(config.getString(key + ".Location.World"));
                double x = config.getDouble(uuid + ".Location.X");
                double y = config.getDouble(uuid + ".Location.Y");
                double z = config.getDouble(uuid + ".Location.Z");
                Location loc = new Location(world, x, y, z);

                requests.add(new Request(uuid, title, loc, status));
            }
        }
    }

    /**
     * Loads the data of the mappings from the yaml file.
     */
    private void loadMappings() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Mappings");
        if (config != null) {
            for (String key : config.getKeys(false)) {

                // uuid
                UUID uuid = UUID.fromString(key);

                // owned
                List<Title> owned = new ArrayList<>();
                List<String> configOwned = config.getStringList(key + ".Owned");
                for (Title t : titles) {
                    for (String name : configOwned) {
                        if (t.getId().equalsIgnoreCase(name)) owned.add(t);
                    }
                }

                // current
                Title current = null;
                for (Title t : titles) {
                    if (t.getId().equalsIgnoreCase(config.getString(key + ".Current"))) {
                        current = t;
                        break;
                    }
                }

                mappings.add(new Mapping(uuid, owned, current));
            }
        }
    }

    //-------------------------------------------------------------- Saving ------------------------------------------------------------------------

    /**
     * Saves all local data to the yaml file.
     */
    public void saveStorage() {
        saveSettings();
        saveCategories();
        saveTitles();
        saveMappings();
        saveRequests();
        plugin.saveConfig();
    }

    private void saveSettings() {
        //TODO: Implement
    }

    /**
     * Saves all local data of the categories to the yaml file.
     */
    private void saveCategories() {
        Configuration config = plugin.getConfig();
        config.set("Categories" ,null);
        for (Category category : categories) {
            config.set(Utils.CATEGORIES + category.getId() + ".Name", category.getName());
            config.set(Utils.CATEGORIES + category.getId() + ".Description", category.getDescription());
        }
    }

    /**
     * Saves all local data of the titles to the yaml file.
     */
    private void saveTitles() {
        Configuration config = plugin.getConfig();
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
     * Saves all local data of the requests to the yaml file.
     */
    private void saveRequests() {
        Configuration config = plugin.getConfig();
        for (Request request : requests) {
            String uuid = request.getUuid().toString();
            config.set(Utils.REQUESTS + uuid + ".Title", request.getTitle().getId());
            config.set(Utils.REQUESTS + uuid + ".Status", request.getStatus().toString());

            Location loc = request.getLocation();
            config.set(Utils.REQUESTS + uuid + ".Location.World", loc.getWorld().getName());
            config.set(Utils.REQUESTS + uuid + ".Location.X", loc.getX());
            config.set(Utils.REQUESTS + uuid + ".Location.Y", loc.getY());
            config.set(Utils.REQUESTS + uuid + ".Location.Z", loc.getZ());
        }
    }

    /**
     * Saves all local data of the mappings to the yaml file.
     */
    private void saveMappings() {
        Configuration config = plugin.getConfig();
        for (Mapping mapping : mappings) {
            String uuid = mapping.getUuid().toString();

            List<String> owned = new ArrayList<>();
            for (Title title : mapping.getOwned()) {
                owned.add(title.getId());
            }
            config.set(Utils.MAPPINGS + uuid + ".Owned", owned);

            if (mapping.getCurrent() != null) {
                config.set(Utils.MAPPINGS + uuid + ".Current", mapping.getCurrent().getId());
            } else {
                config.set(Utils.MAPPINGS + uuid + ".Current", null);
            }
        }
    }

}
