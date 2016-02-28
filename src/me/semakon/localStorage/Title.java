package me.semakon.localStorage;

import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class Title {

    private String id;
    private String name;
    private String description;
    private Category category;

    // title options
    private boolean unique;
    private UUID uniqueTo;

    /**
     * Construct a title with an id, name, description and category. This title is not unique.
     * @param id Unique ID of the title.
     * @param name Title's display name.
     * @param description Description of the title.
     * @param category Category this title belongs to.
     */
    public Title(String id, String name, String description, Category category) {
        this(id, name, description, category, false);
    }

    /**
     * Construct a title with an id, name, description and category. This title can be unique, but no one owns it yet.
     * @param id Unique ID of the title.
     * @param name Title's display name.
     * @param description Description of the title.
     * @param category Category this title belongs to.
     * @param unique Boolean deciding whether this title is unique to one player or not.
     */
    public Title(String id, String name, String description, Category category, boolean unique) {
        this(id, name, description, category, unique, null);
    }

    /**
     * Construct a title with an id, name, description and category. This title can be unique and if it is,
     * is owned by uniqueTo.
     * @param id Unique ID of the title.
     * @param name Title's display name.
     * @param description Description of the title.
     * @param category Category this title belongs to.
     * @param unique Boolean deciding whether this title is unique to one player or not.
     */
    public Title(String id, String name, String description, Category category, boolean unique, UUID uniqueTo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;

        // title options
        this.unique = unique;
        this.uniqueTo = unique ? uniqueTo : null;
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

    public Category getCategory() {
        return category;
    }

    public boolean isUnique() {
        return unique;
    }

    /**
     * UniqueTo is always null if unique is false.
     * @return Null if unique is false.
     */
    public UUID getUniqueTo() {
        return unique ? uniqueTo : null;
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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
        this.uniqueTo = null;
    }

    public void setUniqueTo(UUID player) {
        uniqueTo = unique ? player : null;
    }

    public String toString() {
        return "{" + id + ", " + name + ", " + category.getName() + ChatColor.RESET + ", " + unique + (unique ? (", " + uniqueTo) : "") + "}";
    }

}
