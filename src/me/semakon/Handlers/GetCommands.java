package me.semakon.Handlers;

import me.semakon.Utils;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Mapping;
import me.semakon.localStorage.Request;
import me.semakon.localStorage.Title;
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
        // for every available title add it to the list.
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
    public static List<String> getTitlesFromCategory(DataContainer dc, String category) {
        List<String> titles = new ArrayList<>();
        // for every available title:
        for (Title title : dc.getTitles()) {
            // if the category of title is equal to parameter category, add it to the list.
            if (title.getCategory().equalsIgnoreCase(category)) {
                titles.add(title.getName());
            }
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
        // for every mapping:
        for (Mapping mapping : dc.getMappings()) {
            // if correct player is found:
            if (mapping.getUuid().equals(player.getUniqueId())) {
                // for every title the player owns add it to the list.
                for (Title title : mapping.getOwned()) {
                    titles.add(title.getName());
                }
            }
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
        // for every available title
        for (Title title : dc.getTitles()) {
            // get the category and add it to the list if it's not already in it.
            if (!categories.contains(title.getCategory())) categories.add(title.getCategory());
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
            // add the name of the player of the request and the title's name to the list.
            requests.add(Bukkit.getOfflinePlayer(request.getUuid()).getName() + ": " + request.getTitle().getName());
        }
        return requests;
    }

    /**
     * Returns the key's value from a title.
     * @param dc Container with all data.
     * @param id Id of title where key's value is to be returned from.
     * @param key Key with value.
     * @return A specified key's value from a specified title.
     */
    public static String getFromTitle(DataContainer dc, String id, String key) {
        // for every available title:
        for (Title title : dc.getTitles()) {
            // if correct title is found:
            if (title.getId().equalsIgnoreCase(id)) {
                // return key from title
                if (key.equals(Utils.DESC)) return title.getDescription();
                else if (key.equals(Utils.CAT)) return title.getCategory();
            }
        }
        return null;
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
            if (request.getUuid().equals(player.getUniqueId())) {
                return player.getName() + ": " + request.getTitle().getName();
            }
        }
        return null;
    }

}
