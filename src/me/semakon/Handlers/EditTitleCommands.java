package me.semakon.Handlers;

import me.semakon.Utils;
import me.semakon.localStorage.Category;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Exceptions.CannotRemoveDefaultCategoryException;
import me.semakon.localStorage.Title;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class EditTitleCommands {

    /**
     * Constructs a new title with a name, description and category.
     * @param dc Container with all data.
     * @param name Name of the new title.
     * @param description Description of the new title.
     * @param id Category of the new title.
     * @return True if the new title was created successfully.
     */
    public static boolean createTitle(DataContainer dc, String name, String description, String id) {
        // get category object
        Category category;
        if (id == null) {
            category = dc.getDefaultCategory();
        } else {
            category = dc.getCategory(id);
        }
        if (category == null) return false;

        Title title = new Title(Utils.strip(Utils.setColors(name.toLowerCase())), Utils.setColors(name), description, category);

        // debug
        Utils.consolePrint("Id: " + title.getId());
        Utils.consolePrint("Name: " + title.getName());
        Utils.consolePrint("Category: " + title.getCategory().getName());

        dc.addTitle(title);

        // debug
        Utils.consolePrint(dc.getTitles().toString());
        return true;
    }

    /**
     * Removes an existing title
     * @param dc Container with all data.
     * @param id ID of the title to be removed.
     * @return True if the title was removed successfully.
     */
    public static boolean removeTitle(DataContainer dc, String id) {
        Title title = dc.getTitle(id);
        if (title == null) return false;
        dc.removeTitle(title);
        return true;
    }

    /**
     * Removes an existing category and replaces it with the DEFAULT_CATEGORY.
     * @param dc Container with all data.
     * @param id Category that is to be removed.
     * @return True if the category was removed successfully.
     */
    public static boolean removeCategory(DataContainer dc, String id) throws CannotRemoveDefaultCategoryException {
        Category category = dc.getCategory(id);
        if (category == null) return false;
        dc.removeCategory(category);
        return true;
    }

    /**
     * Edits the description of an existing title.
     * @param dc Container with all data.
     * @param id The ID of the title that is edited.
     * @param description The new description.
     * @return True if the description was edited successfully.
     */
    public static boolean editDescription(DataContainer dc, String id, String description) {
        Title title = dc.getTitle(id);
        if (title == null) return false;
        dc.editTitleDescription(title, description);
        return true;
    }

    /**
     * Edits the category of an existing title. If the category doesn't exist, it will be created.
     * @param dc Container with all data.
     * @param id The ID of the title that is edited.
     * @param categoryId The new category.
     * @return True if the category was edited successfully.
     */
    public static boolean editCategory(DataContainer dc, String id, String categoryId) {
        // get title
        Title title = dc.getTitle(id);
        if (title == null) return false;

        // get category
        Category category = dc.getCategory(categoryId);
        if (category == null) return false;

        dc.editTitleCategory(title, category);
        return true;
    }

    /**
     * Gives a title a new name.
     * @param dc Container with all data.
     * @param id The ID of the title to be renamed.
     * @param name The new name of the title.
     * @return True if the title was renamed successfully.
     */
    public static boolean renameTitle(DataContainer dc, String id, String name) {
        Title title = dc.getTitle(id);
        if (title == null) return false;
        dc.renameTitle(title, name);
        return true;
    }

    /**
     * Gives a category a new name and changes all titles with that category to fit.
     * @param dc This TitlesPlugin.
     * @param id Category that is to be renamed.
     * @param name New name of the category.
     * @return True if the category was renamed successfully.
     */
    public static boolean renameCategory(DataContainer dc, String id, String name) {
        Category category = dc.getCategory(id);
        if (category == null) return false;
        dc.renameCategory(category, name);
        return true;
    }

}
