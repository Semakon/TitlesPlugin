package me.semakon;

import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Settings;
import me.semakon.localStorage.Title;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;

/**
 * Author:  Martijn
 * Date:    15-2-2016
 */
public class PlayerListener implements Listener {

    private TitlesPlugin plugin;

    public PlayerListener(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a player uses the chat. Puts the title in front of the player's name. If donator suffixes
     * are on in the Settings, the correct donator suffix will be added to the player.
     * @param e The event that occurs when a player uses the chat.
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        DataContainer dataContainer = plugin.getDataContainer();

        String name;
        if (dataContainer.getCurrentTitle(player) != null) {
            Title title = dataContainer.getCurrentTitle(player);
            if (title != null) {
                name = ChatColor.RESET + "" + ChatColor.ITALIC + title.getName() + " " + ChatColor.RESET + getNickName(player);
            } else name = ChatColor.RESET + getNickName(player);
        } else name = ChatColor.RESET + player.getDisplayName();

        if (Settings.getDonatorSuffixes()) {
            String suffix = "";
            if (player.hasPermission(plugin.donatorSuffix1))
                suffix = String.format("%s [%s$%s]%s", ChatColor.DARK_GRAY, ChatColor.GREEN, ChatColor.DARK_GRAY, ChatColor.RESET);
            if (player.hasPermission(plugin.donatorSuffix2))
                suffix = String.format("%s [%s$$%s]%s", ChatColor.DARK_GRAY, ChatColor.GREEN, ChatColor.DARK_GRAY, ChatColor.RESET);
            if (player.hasPermission(plugin.donatorSuffix3))
                suffix = String.format("%s [%s$$$%s]%s", ChatColor.DARK_GRAY, ChatColor.GREEN, ChatColor.DARK_GRAY, ChatColor.RESET);
            name += suffix;
        }

        player.setDisplayName(name);
    }

    /**
     * Gets the nickname (from Essentials) of a player through the config file in /plugins/Essentials/userdata/.
     * @param player The player whose nickname is queried.
     * @return The player's nickname from Essentials.
     */
    public String getNickName(OfflinePlayer player) {
        YamlConfiguration customConfig;
        String uuid = player.getUniqueId().toString();

        try {
            File configFile = new File(plugin.getDataFolder() + Utils.ESSENTIALS_USERDATA_FOLDER, uuid + ".yml");
            customConfig = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            e.getStackTrace();
            return player.getName();
        }

        String nickName = customConfig.getString("nickname");
        if (nickName == null) {
            return player.getName();
        } else return nickName;
    }

}
