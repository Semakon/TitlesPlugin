package me.semakon;

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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        String name;
        if (plugin.getConfig().getConfigurationSection("Mappings").contains(player.getUniqueId().toString())) {
            String title = plugin.getConfig().getString(Utils.MAPPINGS + player.getUniqueId().toString() + ".Current");
            if (title != null) {
                String titleName = plugin.getConfig().getString(Utils.TITLES + title + Utils.NAME);
                name = ChatColor.RESET + "" + ChatColor.ITALIC + titleName + " " + ChatColor.RESET + getNickName(player);
            } else name = ChatColor.RESET + getNickName(player);
        } else name = ChatColor.RESET + player.getDisplayName();

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
