package me.semakon;

import me.semakon.localStorage.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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
    public static final String CATEGORIES = "Categories.";
    public static final String TITLES = "Titles.";
    public static final String MAPPINGS = "Mappings.";
    public static final String REQUESTS = "Requests.";

    public static Map<String, String> colors;

    /**
     * Initializes the Map <code>colors</code> with the correct values.
     */
    public static void initColors() {
        colors = new HashMap<>();
        colors.put("&0", ChatColor.BLACK.toString());
        colors.put("&1", ChatColor.DARK_BLUE.toString());
        colors.put("&2", ChatColor.DARK_GREEN.toString());
        colors.put("&3", ChatColor.DARK_AQUA.toString());
        colors.put("&4", ChatColor.DARK_RED.toString());
        colors.put("&5", ChatColor.DARK_PURPLE.toString());
        colors.put("&6", ChatColor.GOLD.toString());
        colors.put("&7", ChatColor.GRAY.toString());
        colors.put("&8", ChatColor.DARK_GRAY.toString());
        colors.put("&9", ChatColor.BLUE.toString());
        colors.put("&a", ChatColor.GREEN.toString());
        colors.put("&b", ChatColor.AQUA.toString());
        colors.put("&c", ChatColor.RED.toString());
        colors.put("&d", ChatColor.LIGHT_PURPLE.toString());
        colors.put("&e", ChatColor.YELLOW.toString());
        colors.put("&f", ChatColor.WHITE.toString());
        colors.put("&l", ChatColor.BOLD.toString());
        colors.put("&o", ChatColor.ITALIC.toString());
        colors.put("&n", ChatColor.UNDERLINE.toString());
        colors.put("&k", ChatColor.MAGIC.toString());
        colors.put("&m", ChatColor.STRIKETHROUGH.toString());
        colors.put("&r", ChatColor.RESET.toString());
    }

    public static String strip(String string) {
        String res = string;
        for (String colorCode : colors.values()) {
            res = replace(res, colorCode, "");
        }
        return res;
    }

    /**
     * Replaces all color symbols with usable color symbols in Minecraft in a title.
     * @param string String with wrong symbols.
     * @return String with correct symbols.
     */
    public static String setColors(String string) {
        String res = string;
        for (String key : colors.keySet()) {
            if (res.contains(key)) {
                res = replace(res, key, colors.get(key));
            }
        }
        return res;
    }

    /**
     * Replaces all <code>target</code>'s with <code>newString</code> in <code>title</code>.
     * @param title Title where parts are replaced.
     * @param target String that is replaced by <code>newString</code>.
     * @param newString String that replaces <code>target</code>.
     * @return String with all <code>target</code>'s replaced by <code>newString</code> in <code>title</code>.
     */
    private static String replace(String title, String target, String newString) {
        boolean atEnd = title.endsWith(target);
        String[] split = title.split(target);
        String res = "";
        for (String part : split) {
            res += part + newString;
        }
        if (!atEnd) res = res.substring(0, res.length() - newString.length());
        return res;
    }

    /**
     * Removes all dots from a string.
     * @param string Original String.
     * @return String with all dots removed.
     */
    public static String removeDots(String string) {
        if (string.contains(".")) {
            String[] split = string.split("\\.");
            String res = "";
            for (String s : split) {
                res += s;
            }
            return res;
        } else return string;
    }

    public static void debugMsg(CommandSender receiver, String msg) {
        if (Settings.isDebugging()) {
            msg = "[TP Debug]" + msg;
            if (receiver == null) System.out.println(msg);
            else receiver.sendMessage(msg);
        }
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
            for (int i = colors.length - 1; i >= 0; i--) {
                msg = colors[i] + msg;
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
        prefix = sender instanceof Player ? String.format(prefix, ChatColor.DARK_GRAY, ChatColor.DARK_AQUA, ChatColor.DARK_GRAY, ChatColor.RESET) :
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
            temps[i] = removeDots(args[i]);
        }
        List<String> finish = new ArrayList<>();
        for (int i = 0; i < temps.length; i++) {
            if (temps[i].startsWith("\"")) {
                if (temps[i].endsWith("\"")) {
                    finish.add(temps[i].substring(1, temps[i].length() - 1));
                    continue;
                }
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
