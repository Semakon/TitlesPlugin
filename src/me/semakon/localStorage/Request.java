package me.semakon.localStorage;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class Request {

    private UUID uuid;
    private Title title;
    private Location location;
    private RequestStatus status;

    public Request(UUID uuid, Title title, Location location, RequestStatus status) {
        this.uuid = uuid;
        this.title = title;
        this.location = location;
        this.status = status;
    }

    public Request(UUID uuid, Title title, Location location) {
        this.uuid = uuid;
        this.title = title;
        this.location = location;
        this.status = RequestStatus.pending;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Title getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String toString() {
        return "{" + uuid.toString() + ", " + title.getName() + ", " + location.toString() + "}";
    }

}
