package me.semakon.localStorage;

import java.util.List;
import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class UserData {

    private UUID uuid;
    private List<Title> unlocked;
    private Title current;
    private Request request;

    public UserData(UUID uuid, List<Title> unlocked, Title current) {
        this(uuid, unlocked, current, null);
    }

    public UserData(UUID uuid, List<Title> unlocked, Title current, Request request) {
        this.uuid = uuid;
        this.unlocked = unlocked;
        this.current = current;
        this.request = request;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Title> getUnlocked() {
        return unlocked;
    }

    public Title getCurrent() {
        return current;
    }

    public Request getRequest() {
        return request;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUnlocked(List<Title> unlocked) {
        this.unlocked = unlocked;
    }

    public void setCurrent(Title current) {
        this.current = current;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String toString() {
        String res = "{" + uuid + ",\n";
        for (Title t : unlocked) {
            res += t.getName() + ",\n";
        }
        if (current == null) return res + null + "}";
        return res + "(Current: " + current.getName() + ")}";
    }

}
