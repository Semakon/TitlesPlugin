package me.semakon.localStorage;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import me.semakon.localStorage.Exceptions.InvalidTitleException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
    private List<Title> titles;
    private List<Request> requests;
    private List<Mapping> mappings;

    public DataContainer(TitlesPlugin plugin) {
        this.plugin = plugin;
        titles = new ArrayList<>();
        requests = new ArrayList<>();
        mappings = new ArrayList<>();
    }

    public List<Title> getTitles() {
        return titles;
    }

    public void createTitle(Title title) {
        titles.add(title);
    }

    /**
     * Gets a list of all titles from a certain category.
     * @param category Category the titles are from.
     * @return A list of titles from a category.
     */
    public List<Title> getTitlesFromCategory(String category) {
        List<Title> titles = new ArrayList<>();
        for (Title title : this.titles) {
            if (title.getCategory().equalsIgnoreCase(category)) titles.add(title);
        }
        return titles;
    }

    public List<Request> getRequests() {
        return requests;
    }

    /**
     * Gets the title that a player requested.
     * @param player The player that requested the title.
     * @return Title that is requested, or if it doesn't exist null.
     */
    public Title getRequest(OfflinePlayer player) {
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

    public List<Mapping> getMappings() {
        return mappings;
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
     * Loads all data from the yaml file to the local storage.
     */
    public void loadStorage() {
        loadTitles();
        loadMappings();
        try {
            loadRequests();
        } catch (InvalidTitleException e) {
            e.getStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin); // too drastic?
        }

        // debug
        Utils.consolePrint("Titles: " + titles);
        Utils.consolePrint("Requests: " + requests);
        Utils.consolePrint("Mappings: " + mappings);
    }

    /**
     * Saves all local data to the yaml file.
     */
    public void saveStorage() {
        saveTitles();
        saveMappings();
        saveRequests();
        plugin.saveConfig();
    }

    /**
     * Loads the data of the titles from the yaml file.
     */
    private void loadTitles() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        if (config != null) {
            for (String key : config.getKeys(false)) {
                // key is identifier.
                String name = config.getString(key + ".Name");
                String description = config.getString(key + ".Description");
                String category = config.getString(key + ".Category");
                titles.add(new Title(key, name, description, category));
            }
        }
    }

    /**
     * Loads the data of the requests from the yaml file.
     */
    private void loadRequests() throws InvalidTitleException {
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
                if (title == null) throw new InvalidTitleException();

                // location
                World world = Bukkit.getWorld(config.getString(key + ".Location.World"));
                double x = config.getDouble(uuid + ".Location.X");
                double y = config.getDouble(uuid + ".Location.Y");
                double z = config.getDouble(uuid + ".Location.Z");
                Location loc = new Location(world, x, y, z);

                requests.add(new Request(uuid, title, loc));
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

    /**
     * Saves all local data of the titles to the yaml file.
     */
    private void saveTitles() {
        ConfigurationSection config = plugin.getConfig();
        for (Title title : titles) {
            config.set(Utils.TITLES + title.getId() + ".Name", title.getName());
            config.set(Utils.TITLES + title.getId() + ".Description", title.getDescription());
            config.set(Utils.TITLES + title.getId() + ".Category", title.getCategory());
        }
    }

    /**
     * Saves all local data of the requests to the yaml file.
     */
    private void saveRequests() {
        ConfigurationSection config = plugin.getConfig();
        for (Request request : requests) {
            config.set(Utils.REQUESTS + request.getUuid().toString() + ".Title", request.getTitle());
            Location loc = request.getLocation();
            config.set(Utils.REQUESTS + request.getUuid().toString() + ".Location.World", loc.getWorld().getName());
            config.set(Utils.REQUESTS + request.getUuid().toString() + ".Location.X", loc.getX());
            config.set(Utils.REQUESTS + request.getUuid().toString() + ".Location.Y", loc.getY());
            config.set(Utils.REQUESTS + request.getUuid().toString() + ".Location.Z", loc.getZ());
        }
    }

    /**
     * Saves all local data of the mappings to the yaml file.
     */
    private void saveMappings() {
        ConfigurationSection config = plugin.getConfig();
        for (Mapping mapping : mappings) {
            List<String> owned = new ArrayList<>();
            for (Title title : mapping.getOwned()) {
                owned.add(title.getId());
            }
            config.set(Utils.MAPPINGS + mapping.getUuid().toString() + ".Owned", owned);
            config.set(Utils.MAPPINGS + mapping.getUuid().toString() + ".Current", mapping.getCurrent());
        }
    }

}
