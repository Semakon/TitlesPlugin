package me.semakon.Handlers;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Author:  Martijn
 * Date:    15-2-2016
 */
public class SetCommands {

    private TitlesPlugin plugin;

    public SetCommands(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets the current title of a player to <code>title</code>. If the player does not own the title or if it
     * doesn't exist, an error will be sent.
     * @param player The player who will get the title.
     * @param title The title the player will get.
     */
    public void setTitle(Player player, String title) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Mappings");
        String uuid = player.getUniqueId().toString();
        if (config.contains(uuid) && plugin.getConfig().getConfigurationSection("Titles").contains(title)) {

            List<String> owned = config.getConfigurationSection(uuid).getStringList("Owned");

            owned.stream().filter(t -> t.equalsIgnoreCase(title)).forEach(t -> {
                config.getConfigurationSection(uuid).set("Current", title);
                plugin.saveConfig();
                player.sendMessage("Title set to " + ChatColor.ITALIC + title + ChatColor.RESET + ".");
                return;
            });
            Utils.sendError(player, "You don't own that title.");
        } else Utils.sendError(player, "You don't own that title or it doesn't exist.");
    }

    public void disableTitle(Player player) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Mappings");
        String uuid = player.getUniqueId().toString();

        if (config.contains(uuid)) {
            config.getConfigurationSection(uuid).set("Current", null);
            plugin.saveConfig();
        }
    }

}
