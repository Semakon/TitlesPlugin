package me.semakon.Handlers;

import me.semakon.Utils;
import me.semakon.localStorage.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class GetCommands {

    /**
     * Returns a list of all available titles.
     * @param dc Container with all data.
     * @return List of all titles.
     */
    public static List<String> getTitles(DataContainer dc) {
        List<String> titles = new ArrayList<>();
        // for every available title add its name to the list.
        for (Title title : dc.getTitles()) {
            titles.add(title.getName());
        }
        return titles;
    }

    /**
     * Returns a list of all titles in a specified category.
     * @param dc Container with all data.
     * @param category Category where titles are to be listed from.
     * @return List of all titles from a specified category.
     */
    public static List<String> getTitlesFromCategory(DataContainer dc, Category category) {
        List<String> titles = new ArrayList<>();

        // for every available title from category, add its name to the list.
        for (Title title : dc.getTitlesFromCategory(category)) {
            titles.add(title.getName());
        }
        return titles;
    }

    /**
     * Creates a list of all titles a player owns..
     * @param dc Container with all data.
     * @param player Player whose owned titles are queried.
     * @return List of all owned titles of a player.
     */
    public static List<String> getMapping(DataContainer dc, OfflinePlayer player) {
        List<String> titles = new ArrayList<>();
        for (Title title : dc.getOwnedTitles(player)) {
            titles.add(title.getName());
        }
        return titles;
    }

    /**
     * Returns a list of all categories.
     * @param dc Container with all data.
     * @return List of all categories.
     */
    public static List<String> getCategories(DataContainer dc) {
        List<String> categories = new ArrayList<>();
        // for every category
        for (Category category : dc.getCategories()) {
            // get the category's name and add it to the list.
            categories.add(category.getName());
        }
        return categories;
    }

    /**
     * Returns a list of all pending requests.
     * @param dc Container with all data.
     * @return List of all pending requests.
     */
    public static List<String> getRequests(DataContainer dc) {
        List<String> requests = new ArrayList<>();
        // for every pending request:
        for (Request request : dc.getRequests()) {
            // if the request is pending add the name of the player of the request and the title's name to the list.
            if (request.getStatus() == RequestStatus.pending) {
                requests.add(Bukkit.getOfflinePlayer(request.getUuid()).getName() + ": " + request.getTitle().getName());
            }
        }
        return requests;
    }

    /**
     * Returns the pending request of the player.
     * @param dc Container with all data.
     * @param player Player whose request is queried.
     * @return The pending request of a player.
     */
    public static String getRequest(DataContainer dc, OfflinePlayer player) {
        // for every pending request:
        for (Request request : dc.getRequests()) {
            // if the request of the player is found, return the player's name and the title
            if (request.getUuid().equals(player.getUniqueId()) && request.getStatus() == RequestStatus.pending) {
                return player.getName() + ": " + request.getTitle().getName();
            }
        }
        return null;
    }

}
