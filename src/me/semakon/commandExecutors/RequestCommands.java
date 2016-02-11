package me.semakon.commandExecutors;

import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class RequestCommands implements CommandExecutor {

    private TitlesPlugin plugin;

    public RequestCommands(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a command is executed.
     * @param sender Sender of the command.
     * @param cmd Command executed.
     * @param label Alias of the command which was used.
     * @param args Arguments given with the command.
     * @return True if the command has been executed successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(plugin.pm2)) {

                FileConfiguration config = plugin.getConfig();

                switch (cmd.getName()) {
                    case Utils.VIEW_REQUESTS:
                        String requests = String.format("%sPending requests:\n%s", ChatColor.GOLD, ChatColor.RESET);
                        for (String key : config.getConfigurationSection("Requests").getKeys(false)) {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(key));
                            String name = op.getName();
                            requests += String.format("(%s) %s: %s\n", key, name, config.getString(Utils.REQUESTS + key));
                        }
                        player.sendMessage(requests);
                        return true;
                }
            } else player.sendMessage(ChatColor.RED + "You don't have permission to perform this action!");
        }
        return false;
    }
}
