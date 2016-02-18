package me.semakon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    9-2-2016
 *
 * Utility class that provides the other classes with static fields and methods.
 */
public class Utils {

    /**
     * Commands:
     */
    public static final String TITLES_COMMAND = "titles";

    /**
     * OP user commands:
     */
    public static final String CREATE_NEW_TITLE = "create";
    public static final String REMOVE_TITLE = "remove";
    public static final String EDIT_DESCRIPTION = "edit_description";

    public static final String VIEW_REQUESTS = "view_requests";

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
     * Replaces all underscores ("_") with spaces (" ") in a String.
     * @param string Original String.
     * @return String with all underscores replaced by spaces.
     */
    public static String addSpaces(String string) {
        if (string.contains("_")) {
            String[] split = string.split("_");
            String res = "";
            for (String s : split) {
                res += s + " ";
            }
            return res.substring(0, res.length() - 1);
        } else return string;
    }

    /**
     * Replaces all target Strings with replacement Strings in a String.
     * @param string Original String.
     * @param target Target String to be replaced.
     * @param replacement String that replaces the target String.
     * @return String with all target Strings replaced by replacement Strings.
     */
    public static String replace(String string, String target, String replacement) {
        if (string.contains(target)) {
            String[] split = string.split(target);
            String res = "";
            for (String s : split) {
                res += s + replacement;
            }
            return res.substring(0, res.length() - 1);
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
     * Sends an error message to the player.
     * @param p The player.
     * @param msg The error message.
     */
    public static void sendError(Player p, String msg) {
        p.sendMessage(ChatColor.RED + "ERROR: " + msg);
    }

    /**
     * Prints a message to the console.
     * @param msg Message to be printed.
     */
    public static void consolePrint(String msg) {
        System.out.println("[TitlesPlugin] " + msg);
    }

}
