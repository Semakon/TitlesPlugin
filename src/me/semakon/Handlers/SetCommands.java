package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Author:  Martijn
 * Date:    15-2-2016
 *
 * Class that contains static methods that deal with the adding, removing and setting of titles to a user.
 * Every class returns a String that contains information about the result of the method.
 */
public class SetCommands {

    /**
     * Sets the current title of a player to <code>title</code>. If the player does not own the title or if it
     * doesn't exist, an error will be sent. Returns a response message containing information about the result of this method.
     * @param player The player who will get the title.
     * @param title The title the player will get.
     * @return A response message containing information about the result of this method.
     */
    public static String setTitle(TitlesPlugin plugin, Player player, String title) {
        System.out.println("title: " + title);
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection("Mappings");
        String uuid = player.getUniqueId().toString();

        // if player has the title and it exists, set player's current title to (parameter)title.
        if (mapConfig.contains(uuid) && plugin.getConfig().getConfigurationSection("Titles").contains(title)) {

            List<String> owned = mapConfig.getConfigurationSection(uuid).getStringList("Owned");
            if (owned.isEmpty()) {
                owned.add(mapConfig.getString(uuid + ".Owned"));
            }
            for (String t : owned) {
                System.out.println("t: " + t);
                if (t.equalsIgnoreCase(title)) {
                    mapConfig.getConfigurationSection(uuid).set("Current", title);
                    plugin.saveConfig();
                    return "Title set to %s" + title + "%s.";
                }
            }
            return "You don't own that title.";
        } else return "You don't own that title or it doesn't exist.";
    }

    /**
     * Disables the player's current title if they have one. Returns a response message containing
     * information about the result of this method.
     * @param plugin This plugin.
     * @param player Player whose title will be disabled.
     * @return A response message containing information about the result of this method.
     */
    public static String disableTitle(TitlesPlugin plugin, OfflinePlayer player) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Mappings");
        String uuid = player.getUniqueId().toString();

        // if the player has the title enabled, disable it.
        if (config.contains(uuid)) {
            config.getConfigurationSection(uuid).set("Current", null);
            plugin.saveConfig();
            return "Title disabled.";
        }
        return "Player not found.";
    }

    /**
     * Adds a title to a player's list of owned titles. Returns a message with information about
     * the result of this method.
     * @param plugin This plugin.
     * @param player Player who will receive a new title.
     * @param title Title that will be added.
     * @return A response message containing information about the result of this method.
     */
    public static String addTitle(TitlesPlugin plugin, OfflinePlayer player, String title) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection(Utils.MAPPINGS + uuid);
        ConfigurationSection titlesConfig = plugin.getConfig().getConfigurationSection("Titles");

        // if title exists and player doesn't already have title, add title to player's owned list.
        if (titlesConfig.contains(title.toLowerCase()) && !mapConfig.getString("Owned").contains(title.toLowerCase())) {
            List<String> stringList = mapConfig.getStringList("Owned");
            stringList.add(title.toLowerCase());
            mapConfig.set("Owned", stringList);
            plugin.saveConfig();
            return "Added %s" + title + "%s to " + player.getName() + "'s owned titles.";
        } else return "Title doesn't exist or player already has that title.";
    }

    /**
     * Removes a title from a player's list of owned titles. Returns a message with information about
     * the result of this method.
     * @param plugin This plugin.
     * @param player Player whose title will be removed.
     * @param title Title that will be removed.
     * @return A response message containing information about the result of this method.
     */
    public static String removeTitle(TitlesPlugin plugin, OfflinePlayer player, String title) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection(Utils.MAPPINGS + uuid);

        // if player owns the title, remove it.
        if (mapConfig.getStringList("Owned").contains(title.toLowerCase())) {
            List<String> stringList = mapConfig.getStringList("Owned");
            stringList.remove(title.toLowerCase());
            mapConfig.set("Owned", stringList);
            if (mapConfig.getString("Current").equalsIgnoreCase(title)) {
                disableTitle(plugin, player);
            }
            plugin.saveConfig();
            return "Removed %s" + title + "%s from " + player.getName() + "'s owned titles.";
        } else return "Player doesn't own that title.";
    }


}
