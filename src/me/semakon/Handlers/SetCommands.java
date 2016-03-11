package me.semakon.Handlers;

import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.UserData;
import me.semakon.localStorage.Title;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Class that contains static methods that deal with the adding, removing and setting of titles to a user.
 *
 * Author:  Martijn
 * Date:    15-2-2016
 */
public class SetCommands {

    /**
     * Sets the current title of a player to <code>title</code>. If the player does not own the title or if it
     * doesn't exist, an error will be sent.
     * @param dc Container with all data.
     * @param player The player who will get the title.
     * @param id The ID of the title the player will get.
     * @return True if the title has been set.
     */
    public static boolean setTitle(DataContainer dc, Player player, String id) {
        // get title
        Title title = dc.getTitle(id);
        if (title == null) return false;

        for (Title t : dc.getUnlockedTitles(player)) {
            if (title.equals(t)) {
                dc.setCurrentTitle(player, t);
                return true;
            }
        }
        return false;
    }

    /**
     * Disables the player's current title if they have one.
     * @param dc Container with all data.
     * @param player Player whose title will be disabled.
     * @return True if the title has been disabled.
     */
    public static boolean disableTitle(DataContainer dc, OfflinePlayer player) {
        for (UserData userData : dc.getUserData()) {
            if (userData.getUuid().equals(player.getUniqueId())) {
                userData.setCurrent(null);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a title to a player's list of owned titles.
     * @param dc Container with all data.
     * @param player Player who will receive a new title.
     * @param id The ID of the Title that will be added.
     * @return True if the title has been added.
     */
    public static boolean addTitle(DataContainer dc, OfflinePlayer player, String id) {
        // get title
        Title title = dc.getTitle(id);
        if (title == null) return false;

        // if title is unique and already owned by another player
        if (title.isUnique() && title.getUniqueTo() != null) return false;

        List <Title> owned = dc.getUnlockedTitles(player);
        if (!owned.contains(title)) {
            dc.addToUnlockedTitles(player, title);
            return true;
        }
        return false;
    }

    /**
     * Removes a title from a player's list of owned titles.
     * @param dc Container with all data.
     * @param player Player whose title will be removed.
     * @param id The ID of the Title that will be removed.
     * @return True if the title has been removed.
     */
    public static boolean removeTitle(DataContainer dc, OfflinePlayer player, String id) {
        // get title
        Title title = dc.getTitle(id);
        if (title == null) return false;

        // if player doesn't own that title
        if (!dc.getUnlockedTitles(player).contains(title)) return false;

        // remove title from player's owned list
        dc.removeFromUnlockedTitles(player, title);

        // if title is equipped, unequip it.
        if (dc.getCurrentTitle(player).equals(title)) dc.setCurrentTitle(player, null);
        return true;
    }


}
