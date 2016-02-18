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

    public static boolean approveRequest(TitlesPlugin plugin, OfflinePlayer player, String title) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Requests");

        // if the player has a request.
        if (config.getKeys(false).contains(uuid)) {
            if (SetCommands.addTitle(plugin, player, title)) {
                config.set(uuid, null);
                plugin.saveConfig();
                return true;
            }
        }
        return false;
    }

    public static boolean submitRequest(TitlesPlugin plugin, Player player, String title, String comments) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection requestsConfig = plugin.getConfig().getConfigurationSection("Requests");
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");

        // if player doesn't already have a pending request and the title exists.
        if (!requestsConfig.getKeys(false).contains(uuid) && titlesConfig.getKeys(false).contains(title.toLowerCase())){
            requestsConfig.set(uuid + ".Title", title);
            requestsConfig.set(uuid + ".Comments", comments);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

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
