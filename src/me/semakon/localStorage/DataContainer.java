package me.semakon.localStorage;

import me.semakon.TitlesPlugin;
import me.semakon.localStorage.Exceptions.InvalidTitleException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    public List<Request> getRequests() {
        return requests;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void loadStorage() {
        loadTitles();
        loadMappings();
        try {
            loadRequests();
        } catch (InvalidTitleException e) {
            e.getStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin); // too drastic?
        }
    }

    public void saveStorage() {
        saveTitles();
        saveMappings();
        saveRequests();
        plugin.saveConfig();
    }

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

    private void saveTitles() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        for (Title title : titles) {
            config.set(title.getId() + ".Name", title.getName());
            config.set(title.getId() + ".Description", title.getDescription());
            config.set(title.getId() + ".Category", title.getCategory());
        }
    }

    private void saveRequests() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");
        for (Request request : requests) {
            config.set(request.getUuid().toString() + ".Title", request.getTitle());
            Location loc = request.getLocation();
            config.set(request.getUuid().toString() + ".Location.World", loc.getWorld().getName());
            config.set(request.getUuid().toString() + ".Location.X", loc.getX());
            config.set(request.getUuid().toString() + ".Location.Y", loc.getY());
            config.set(request.getUuid().toString() + ".Location.Z", loc.getZ());
        }
    }

    private void saveMappings() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Mappings");
        for (Mapping mapping : mappings) {
            List<String> owned = new ArrayList<>();
            for (Title title : mapping.getOwned()) {
                owned.add(title.getId());
            }
            config.set(mapping.getUuid().toString() + ".Owned", owned);
            config.set(mapping.getUuid().toString() + ".Current", mapping.getCurrent());
        }
    }

}
