package me.semakon.localStorage;

import me.semakon.Utils;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class Title {

    private String id;
    private String name;
    private String description;
    private String category;

    public Title(String id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Title(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = Utils.DEFAULT_CATEGORY;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return "{" + id + ", " + name + ", " + description + ", " + category + "}";
    }

}
