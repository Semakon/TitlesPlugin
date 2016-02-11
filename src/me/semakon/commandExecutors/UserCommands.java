package me.semakon.commandExecutors;

import me.semakon.Handlers.GetHandler;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    10-2-2016
 */
public class UserCommands implements CommandExecutor {

    private TitlesPlugin plugin;

    public UserCommands(TitlesPlugin plugin) {
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
            FileConfiguration config = plugin.getConfig();

            switch (cmd.getName()) {

                case Utils.GET_TITLES:
                    String res;
                    List<String> titles;
                    if (args.length != 0) {
                        res = String.format("%sAvailable titles in %s:\n%s", ChatColor.GOLD, args[0], ChatColor.RESET);
                        titles = GetHandler.getTitlesFromCategory(plugin, args[0]);
                    } else {
                        res = String.format("%sAvailable titles:\n%s", ChatColor.GOLD, ChatColor.RESET);
                        titles = GetHandler.getTitles(plugin);
                    }
                    for (String title : titles) {
                        res += title + "\n";
                    }
                    player.sendMessage(res);
                    return true;

                //TODO: implement GetHandler on cases below
                case Utils.GET_DESCRIPTION:
                    if (args.length == 1) {
                        if (config.contains(Utils.TITLES + args[0].toLowerCase() + Utils.DESC)) {
                            player.sendMessage(Utils.createSpaces(config.getString(Utils.TITLES + args[0].toLowerCase() + Utils.DESC)));
                        }
                        return true;
                    }
                    break;

                case Utils.UNLOCK_TITLE:
                    if (args.length == 1) {
                        UUID uuid = player.getUniqueId();
                        String title = args[0];
                        System.out.println("uuid: " + uuid + "\ntitle: " + title);
                        if (!config.contains(Utils.REQUESTS + uuid)) {
                            config.set(Utils.REQUESTS + uuid, title.toLowerCase());
                            player.sendMessage("Title request submitted.");
                            plugin.saveConfig();
                            return true;
                        } else player.sendMessage(ChatColor.RED + "You've already submitted a title request.");
                    }
                    break;
            }
        }
        return false;
    }

}
