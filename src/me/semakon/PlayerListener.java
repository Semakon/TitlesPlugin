package me.semakon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
        if (plugin.getConfig().getConfigurationSection(Utils.MAPPINGS).contains(player.getUniqueId().toString())) {
            String title = plugin.getConfig().getConfigurationSection(Utils.MAPPINGS + player.getUniqueId().toString()).getString("Current");
            if (title != null) {
                String titleName = plugin.getConfig().getConfigurationSection(Utils.TITLES + title).getString(Utils.NAME);
                name = ChatColor.ITALIC + Utils.addSpaces(titleName) + " " + ChatColor.RESET + player.getName();
            } else name = player.getName();
        } else name = player.getName();

        player.setDisplayName(name);
    }

}
