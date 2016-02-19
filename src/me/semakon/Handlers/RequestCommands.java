package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    12-2-2016
 */
public class RequestCommands {

    /**
     * Denies the request of a player.
     * @param plugin This plugin.
     * @param player The player whose request has been denied.
     * @return True if the request has been denied successfully.
     */
    public static boolean denyRequest(TitlesPlugin plugin, OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

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
     * @param plugin This plugin.
     * @param player The player whose request has been approved.
     * @return True if the request has been approved successfully.
     */
    public static boolean approveRequest(TitlesPlugin plugin, OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

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
     * Submits a request for a title for a player.
     * @param plugin This plugin.
     * @param player The player who is making the request.
     * @param title The title that is requested.
     * @param comments Comments by the player (why they deserve this title).
     * @return True if the request has been submitted successfully.
     */
    public static boolean submitRequest(TitlesPlugin plugin, Player player, String title, String comments) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection requestsConfig = plugin.getConfig().getConfigurationSection("Requests");
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");

        // if player doesn't already have a pending request and the title exists, add request to requests.
        if (!requestsConfig.getKeys(false).contains(uuid) && titlesConfig.getKeys(false).contains(title.toLowerCase())){
            requestsConfig.set(uuid + ".Title", title);
            requestsConfig.set(uuid + ".Comments", comments);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Retracts a player's pending request for a title.
     * @param plugin This plugin.
     * @param player The player whose request is retracted.
     * @return True if the request has been retracted successfully.
     */
    public static boolean retractRequest(TitlesPlugin plugin, Player player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

        // if player has a pending request, delete data
        if (config.getKeys(false).contains(uuid)) {
            config.set(uuid, null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

}
