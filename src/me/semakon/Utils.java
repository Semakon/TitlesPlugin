package me.semakon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    9-2-2016
 *
 * Utility class that provides the other classes with static fields and methods.
 */
public class Utils {

    /**
     * Normal user commands:
     */
    public static final String GET_TITLES = "titles";
    public static final String GET_DESCRIPTION = "description";
    public static final String GET_CATEGORY = "categories";

    public static final String UNLOCK_TITLE = "unlock_title";
    public static final String CHANGE_TITLE = "change_title";
    public static final String RETRACT_TITLE_REQUEST = "retract_request";

    /**
     * OP user commands:
     */
    public static final String CREATE_NEW_TITLE = "create";
    public static final String REMOVE_TITLE = "remove";
    public static final String EDIT_DESCRIPTION = "edit_description";
    public static final String EDIT_CATEGORY = "edit_category";

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
    public static String createSpaces(String string) {
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
        System.out.println("[TitlePlugin] " + msg);
    }

}
