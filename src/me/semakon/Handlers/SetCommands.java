package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Martijn
 * Date:    15-2-2016
 *
 * Class that contains static methods that deal with the adding, removing and setting of titles to a user.
 */
public class SetCommands {

    /**
     * Sets the current title of a player to <code>title</code>. If the player does not own the title or if it
     * doesn't exist, an error will be sent.
     * @param player The player who will get the title.
     * @param title The title the player will get.
     * @return True if the title has been set.
     */
    public static boolean setTitle(TitlesPlugin plugin, Player player, String title) {
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection("Mappings");

        // if config is empty.
        if (mapConfig == null) {
            return false;
        }

        String uuid = player.getUniqueId().toString();

        // if player has the title and it exists, set player's current title to (parameter)title.
        if (mapConfig.contains(uuid) && plugin.getConfig().getConfigurationSection("Titles").contains(title.toLowerCase())) {

            List<String> owned = mapConfig.getConfigurationSection(uuid).getStringList("Owned");
            if (owned.isEmpty()) {
                owned.add(mapConfig.getString(uuid + ".Owned"));
            }
            for (String t : owned) {
                if (t.equalsIgnoreCase(title.toLowerCase())) {
                    mapConfig.getConfigurationSection(uuid).set("Current", title);
                    plugin.saveConfig();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Disables the player's current title if they have one.
     * @param plugin This plugin.
     * @param player Player whose title will be disabled.
     * @return True if the title has been disabled.
     */
    public static boolean disableTitle(TitlesPlugin plugin, OfflinePlayer player) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Mappings");

        // if config is empty.
        if (config == null) {
            return false;
        }

        String uuid = player.getUniqueId().toString();

        // if the player has the title enabled, disable it.
        if (config.contains(uuid)) {
            config.getConfigurationSection(uuid).set("Current", null);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Adds a title to a player's list of owned titles.
     * @param plugin This plugin.
     * @param player Player who will receive a new title.
     * @param title Title that will be added.
     * @return True if the title has been added.
     */
    public static boolean addTitle(TitlesPlugin plugin, OfflinePlayer player, String title) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection(Utils.MAPPINGS + uuid);
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");

        // if config is empty.
        if (titlesConfig == null) {
            return false;
        }
        // if mappings is not yet in the config.
        if (mapConfig == null) {
            if (titlesConfig.contains(title.toLowerCase())) {
                List<String> owned = new ArrayList<>();
                owned.add(title.toLowerCase());
                plugin.getConfig().set(Utils.MAPPINGS + uuid + ".Owned", owned);
                plugin.getConfig().set(Utils.MAPPINGS + uuid + ".Current", title.toLowerCase());
                plugin.saveConfig();
                return true;
            } else return false;
        }

        // if title exists and player doesn't already have title, add title to player's owned list.
        if (titlesConfig.contains(title.toLowerCase()) && !mapConfig.getString("Owned").contains(title.toLowerCase())) {
            List<String> stringList = mapConfig.getStringList("Owned");
            stringList.add(title.toLowerCase());
            mapConfig.set("Owned", stringList);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Removes a title from a player's list of owned titles.
     * @param plugin This plugin.
     * @param player Player whose title will be removed.
     * @param title Title that will be removed.
     * @return True if the title has been removed.
     */
    public static boolean removeTitle(TitlesPlugin plugin, OfflinePlayer player, String title) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection(Utils.MAPPINGS + uuid);

        // if config is empty.
        if (mapConfig == null) {
            return false;
        }

        // if player owns the title, remove it.
        if (mapConfig.getStringList("Owned").contains(title.toLowerCase())) {
            List<String> stringList = mapConfig.getStringList("Owned");
            stringList.remove(title.toLowerCase());
            mapConfig.set("Owned", stringList);
            if (mapConfig.getString("Current").equalsIgnoreCase(title)) {
                disableTitle(plugin, player);
            }
            plugin.saveConfig();
            return true;
        }
        return false;
    }


}
