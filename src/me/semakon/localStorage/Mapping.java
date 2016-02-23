package me.semakon.localStorage;

import java.util.List;
import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class Mapping {

    private UUID uuid;
    private List<Title> owned;
    private Title current;

    public Mapping(UUID uuid, List<Title> owned, Title current) {
        this.uuid = uuid;
        this.owned = owned;
        this.current = current;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Title> getOwned() {
        return owned;
    }

    public Title getCurrent() {
        return current;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setOwned(List<Title> owned) {
        this.owned = owned;
    }

    public void setCurrent(Title current) {
        this.current = current;
    }

    public String toString() {
        String res = "{" + uuid + ",\n";
        for (Title t : owned) {
            res += t.getName() + ",\n";
        }
        if (current == null) return res + null + "}";
        return res + "(Current: " + current.getName() + ")}";
    }

}
