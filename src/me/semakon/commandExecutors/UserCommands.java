package me.semakon.commandExecutors;

import me.semakon.Handlers.GetCommands;
import me.semakon.Handlers.SetCommands;
import me.semakon.TitlesPlugin;
import me.semakon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

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
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        SetCommands sh = new SetCommands(plugin);
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");

        switch (cmd.getName()) {

            case Utils.GET_TITLES:
                String res;
                List<String> titles;
                if (args.length != 0) {
                    String category = args[0];
                    res = String.format("%sAvailable titles in %s:\n%s", ChatColor.GOLD, category, ChatColor.RESET);
                    titles = GetCommands.getTitlesFromCategory(config, category);
                } else {
                    res = String.format("%sAvailable titles:\n%s", ChatColor.GOLD, ChatColor.RESET);
                    titles = GetCommands.getTitles(config);
                }
                for (String title : titles) res += title + "\n";

                if (player == null) Utils.consolePrint(res);
                else player.sendMessage(res);

                return true;

            case Utils.GET_DESCRIPTION:
                if (args.length == 1) {
                    String title = args[0];
                    String description = GetCommands.getFromTitle(config, title, Utils.DESC);
                    if (description == null) {
                        if (player == null) Utils.consolePrint("That title doesn't exist.");
                        else Utils.sendError(player, "That title doesn't exist.");
                    } else {
                        if (player == null) Utils.consolePrint(description);
                        else player.sendMessage(description);
                        return true;
                    }
                }
                break;

            case "test":
                if (player != null && args.length == 1) {
                    String title = args[0];
                    sh.setTitle(player, title);
                    return true;
                }
                break;

            //TODO: implement RequestHandler on case below
//            case Utils.UNLOCK_TITLE:
//                if (player == null) break;
//                if (args.length == 1) {
//                    UUID uuid = player.getUniqueId();
//                    String title = args[0];
//                    System.out.println("uuid: " + uuid + "\ntitle: " + title);
//                    if (!config.contains(Utils.REQUESTS + uuid)) {
//                        config.set(Utils.REQUESTS + uuid, title.toLowerCase());
//                        player.sendMessage("Title request submitted.");
//                        plugin.saveConfig();
//                        return true;
//                    } else player.sendMessage(ChatColor.RED + "You've already submitted a title request.");
//                }
//                break;
        }

        return false;
    }

}
