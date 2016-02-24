package me.semakon.localStorage;

/**
 * Author:  Martijn
 * Date:    24-2-2016
 */
public enum RequestStatus {

    denied, approved, pending;

    public static RequestStatus fromString(String status) {
        switch(status.toLowerCase()) {
            case "denied":
                return denied;
            case "approved":
                return approved;
            case "pending":
                return pending;
            default:
                return pending;
        }
    }

}
