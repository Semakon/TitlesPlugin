package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    12-2-2016
 */
public class RequestCommands {

    /**
     * Denies the request of a player.
     * @param plugin This TitlesPlugin used to access the config.
     * @param player The player whose request has been denied.
     * @return True if the request has been denied successfully.
     */
    public static boolean denyRequest(TitlesPlugin plugin, OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

        // if config is empty.
        if (config == null) return false;

        // if the player has a request.
        if (config.getKeys(false).contains(uuid)) {
            config.set(uuid, null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Approves the request of a player and adds the title to the player's list of titles.
     * @param plugin This TitlesPlugin used to access the config.
     * @param player The player whose request has been approved.
     * @return True if the request has been approved successfully.
     */
    public static boolean approveRequest(TitlesPlugin plugin, OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

        // if config is empty.
        if (config == null) return false;

        // if the player has a request.
        if (config.getKeys(false).contains(uuid)) {
            if (SetCommands.addTitle(plugin, player, config.getString(uuid + ".Title"))) {
                config.set(uuid, null);
                plugin.saveConfig();
                return true;
            }
        }
        return false;
    }

    /**
     * Teleports the sender of the command to the location where the player submitted their request.
     * @param plugin This TitlesPlugin used to access the config.
     * @param player Player that submitted the request.
     * @param sender Sender of the command.
     * @return True if the sender was teleported successfully.
     */
    public static boolean tpToRequest(TitlesPlugin plugin, OfflinePlayer player, Player sender) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

        // if config is empty.
        if (config == null) return false;

        if (config.getKeys(false).contains(uuid)) {
            World world = Bukkit.getWorld(config.getString(uuid + ".Location.World"));
            double x = config.getDouble(uuid + ".Location.X");
            double y = config.getDouble(uuid + ".Location.Y");
            double z = config.getDouble(uuid + ".Location.Z");
            Location loc = new Location(world, x, y, z);
            sender.teleport(loc);
            return true;
        }
        return false;
    }

    /**
     * Submits a request for a title for a player.
     * @param plugin This TitlesPlugin used to access the config.
     * @param player The player who is making the request.
     * @param title The title that is requested.
     * @return True if the request has been submitted successfully.
     */
    public static boolean submitRequest(TitlesPlugin plugin, Player player, String title) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection requestsConfig = plugin.getConfig().getConfigurationSection("Requests");
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");

        // if config is empty.
        if (titlesConfig == null) return false;

        // if requests is not yet in the config.
        if (requestsConfig == null) {
            if (titlesConfig.contains(title.toLowerCase())) {

                Utils.sendMsg(player, "titlesConfig contains title.toLowerCase");

                plugin.getConfig().set(Utils.REQUESTS + uuid + ".Title", title.toLowerCase());

                Location loc = player.getLocation();
                plugin.getConfig().set(Utils.REQUESTS + uuid + ".Location.World", loc.getWorld().getName());
                plugin.getConfig().set(Utils.REQUESTS + uuid + ".Location.X", loc.getX());
                plugin.getConfig().set(Utils.REQUESTS + uuid + ".Location.Y", loc.getY());
                plugin.getConfig().set(Utils.REQUESTS + uuid + ".Location.Z", loc.getZ());

                plugin.saveConfig();
                return true;
            } else return false;
        }

        // if player doesn't already have a pending request and the title exists, add request to requests.
        if (!requestsConfig.getKeys(false).contains(uuid) && titlesConfig.getKeys(false).contains(title.toLowerCase())){
            requestsConfig.set(uuid + ".Title", title.toLowerCase());

            Location loc = player.getLocation();
            requestsConfig.set(uuid + ".Location.World", loc.getWorld().getName());
            requestsConfig.set(uuid + ".Location.X", loc.getX());
            requestsConfig.set(uuid + ".Location.Y", loc.getY());
            requestsConfig.set(uuid + ".Location.Z", loc.getZ());

            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Retracts a player's pending request for a title.
     * @param plugin This TitlesPlugin used to access the config.
     * @param player The player whose request is retracted.
     * @return True if the request has been retracted successfully.
     */
    public static boolean retractRequest(TitlesPlugin plugin, Player player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

        // if config is empty.
        if (config == null) return false;

        // if player has a pending request, delete data
        if (config.getKeys(false).contains(uuid)) {
            config.set(uuid, null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

}
