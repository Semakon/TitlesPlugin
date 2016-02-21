package me.semakon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    9-2-2016
 *
 * Utility class that provides the other classes with static fields and methods.
 */
public class Utils {

    /**
     * Some useful constants:
     */
    public static final String TITLES_COMMAND = "titles";

    public static final String DEFAULT_CATEGORY = "General";

    public static final String ESSENTIALS_USERDATA_FOLDER = "/../Essentials/userdata/";

    /**
     * YAML file conventions:
     */
    public static final String TITLES = "Titles.";
    public static final String MAPPINGS = "Mappings.";
    public static final String REQUESTS = "Requests.";
    public static final String NAME = ".Name";
    public static final String DESC = ".Description";
    public static final String CAT = ".Category";

    /**
     * Replaces all target Strings with replacement Strings in a String.
     * @param string Original String.
     * @return String with all target Strings replaced by replacement Strings.
     */
    public static String replaceDot(String string) {
        if (string.contains(".")) {
            String[] split = string.split("\\.");
            String res = "";
            for (String s : split) {
                res += s;
            }
            return res;
        } else return string;
    }

    /**
     * Converts a name of an offline player to their unique ID.
     * If the player can't be found, the name is returned instead.
     * @param name Name of target player.
     * @return A player's unique ID or their name.
     */
    public static String nameToUUID(String name) {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName().equals(name)) {
                return player.getUniqueId().toString();
            }
        }
        return name;
    }

    /**
     * Gets an offline player by a name. If the player can't be found, null is returned.
     * @param name Name of player.
     * @return OfflinePlayer or null if it can't be found.
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        String uuid = Utils.nameToUUID(name);
        OfflinePlayer user = null;
        try {
            UUID uidP = UUID.fromString(uuid);
            user = Bukkit.getOfflinePlayer(uidP);
        } catch (IllegalArgumentException e) {
            Utils.consolePrint(e.getMessage());
        }
        return user;
    }

    /**
     * Prints a message to the console.
     * @param msg Message to be printed.
     */
    public static void consolePrint(String msg) {
        System.out.println("[TitlesPlugin] " + msg);
    }

    /**
     * Sends an error message to the player.
     * @param sender The sender of the command. They are also receiver.
     * @param msg The error message.
     */
    public static void sendError(CommandSender sender, String msg) {
        if (sender instanceof Player) msg = ChatColor.RED + msg;
        sender.sendMessage(msgPrefix(sender, msg));
    }

    /**
     * Sends an error message to the player.
     * @param sender The sender of the command. They are also receiver.
     * @param msg The error message.
     */
    public static void sendMsg(CommandSender sender, String msg, ChatColor... colors) {
        if (sender instanceof Player && colors != null) {
            for (ChatColor color : colors) {
                msg = color + msg;
            }
        }
        sender.sendMessage(msgPrefix(sender, msg));
    }

    /**
     * Adds a prefix to a message. If the sender is a player, the prefix will have colors.
     * @param sender Sender of a command.
     * @param msg Message that gets the prefix.
     * @return Message with added prefix.
     */
    private static String msgPrefix(CommandSender sender, String msg) {
        String prefix = "%s[%sTP%s]%s";
        prefix = sender instanceof Player ? String.format(prefix, ChatColor.GRAY, ChatColor.DARK_AQUA, ChatColor.GRAY, ChatColor.RESET) :
                String.format(prefix, "", "", "", "");
        return (prefix + " " + msg);
    }

    /**
     * Sends all Strings in a String array to the sender.
     * @param sender Sender of commands.
     * @param array Array to be sent.
     */
    public static void sendArray(CommandSender sender, String[] array) {
        for (String msg : array) {
            Utils.sendMsg(sender, msg);
        }
    }

    /**
     * Takes an array of arguments. Arguments between two <"> are made into one String.
     * @param args Arguments that are reformatted.
     * @return An array with reformatted arguments.
     */
    public static String[] inQuotes(String[] args) {
        String[] temps = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            temps[i] = replaceDot(args[i]);
        }
        List<String> finish = new ArrayList<>();
        for (int i = 0; i < temps.length; i++) {
            if (temps[i].startsWith("\"")) {
                String string = temps[i].substring(1, temps[i].length());
                boolean add = false;
                for (int k = i + 1; k < temps.length && !add; k++) {
                    if (temps[k].endsWith("\"")) {
                        string += " " + temps[k].substring(0, temps[k].length() - 1);
                        finish.add(string);
                        i = k;
                        add = true;
                    } else string += " " + temps[k];
                }
                if (!add) finish.add(temps[i]);
            } else {
                finish.add(temps[i]);
            }
        }
        return finish.toArray(new String[0]);
    }

}
