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

    public Request(UUID uuid, Title title, Location location) {
        this.uuid = uuid;
        this.title = title;
        this.location = location;
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

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String toString() {
        return "{" + uuid.toString() + ", " + title.getName() + ", " + location.toString() + "}";
    }

}
