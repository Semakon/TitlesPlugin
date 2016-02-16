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
public class SetExecutor {

    private TitlesPlugin plugin;

    public SetExecutor(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        SetCommands sh = new SetCommands(plugin);

        //test
        if (player != null && args.length == 1) {
            String title = args[0];
            sh.setTitle(player, title);
            return true;
        }


            //TODO: implement RequestHandler on case below
//                  /titles request submit <title> [<comments>]
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
//


        return false;
    }

}
