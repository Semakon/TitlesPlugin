package me.semakon.Handlers;

import me.semakon.localStorage.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Author:  Martijn
 * Date:    12-2-2016
 */
public class RequestCommands {

    /**
     * Denies the request of a player.
     * @param dc Container with all data.
     * @param player The player whose request has been denied.
     * @return True if the request has been denied successfully.
     */
    public static boolean denyRequest(DataContainer dc, OfflinePlayer player) {
        Request request = dc.getRequest(player);
        if (request != null && request.getStatus() == RequestStatus.pending) {
            request.setStatus(RequestStatus.denied);
            return true;
        } else return false;
    }

    /**
     * Approves the request of a player and adds the title to the player's list of titles.
     * @param dc Container with all data.
     * @param player The player whose request has been approved.
     * @return True if the request has been approved successfully.
     */
    public static boolean approveRequest(DataContainer dc, OfflinePlayer player) {
        Request request = dc.getRequest(player);
        if (request != null && request.getStatus() == RequestStatus.pending) {
            if (SetCommands.addTitle(dc, player, request.getTitle().getId())) {
                request.setStatus(RequestStatus.approved);
                return true;
            }
        }
        return false;
    }

    /**
     * Teleports the sender of the command to the location where the player submitted their request.
     * @param dc Container with all data.
     * @param player Player that submitted the request.
     * @param sender Sender of the command.
     * @return True if the sender was teleported successfully.
     */
    public static boolean tpToRequest(DataContainer dc, OfflinePlayer player, Player sender) {
        Request request = dc.getRequest(player);
        if (request != null) {
            sender.teleport(request.getLocation());
            return true;
        } else return false;
    }

    /**
     * Submits a request for a title for a player.
     * @param dc Container with all data.
     * @param player The player who is making the request.
     * @param id The ID of the title that is requested.
     * @return True if the request has been submitted successfully.
     */
    public static boolean submitRequest(DataContainer dc, Player player, String id) {
        // get title
        Title title = dc.getTitle(id);
        if (title == null) return false;

        // get or create request
        Request request = dc.getRequest(player);
        if (request == null) {
            request = new Request(player.getUniqueId(), title, player.getLocation());
            for (UserData ud : dc.getUserData()) {
                if (ud.getUuid().equals(player.getUniqueId())) {
                    ud.setRequest(request);
                    return true;
                }
            }
        } else if (request.getStatus() != RequestStatus.pending) {
            request.setTitle(title);
            request.setLocation(player.getLocation());
            request.setStatus(RequestStatus.pending);
            return true;
        }
        return false;
    }

    /**
     * Retracts a player's pending request for a title.
     * @param dc Container with all data.
     * @param player The player whose request is retracted.
     * @return True if the request has been retracted successfully.
     */
    public static boolean retractRequest(DataContainer dc, Player player) {
        Request request = dc.getRequest(player);
        if (request != null && request.getStatus() == RequestStatus.pending) {
            request.setStatus(RequestStatus.nonExistent);
            return true;
        }
        return false;
    }

}
