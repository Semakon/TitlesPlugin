package me.semakon.Handlers;

import me.semakon.Utils;
import me.semakon.localStorage.Category;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Exceptions.CannotRemoveDefaultCategoryException;
import me.semakon.localStorage.Exceptions.InvalidCategoryException;
import me.semakon.localStorage.Title;

/**
 * Author:  Martijn
 * Date:    11-2-2016
 */
public class EditTitleCommands {

    /**
     * Constructs a new title with a name, description and category.
     * @param dc Container with all data.
     * @param titleName Name of the new title.
     * @param description Description of the new title.
     * @param categoryId Category of the new title.
     * @return True if the new title was created successfully.
     */
    public static boolean createTitle(DataContainer dc, String titleName, String description, String categoryId) throws InvalidCategoryException {
        // get category object
        Category category;
        if (categoryId == null) {
            category = dc.getDefaultCategory();
        } else {
            category = dc.getCategory(categoryId);
        }
        if (category == null) throw new InvalidCategoryException();

        // get title object
        String titleId = Utils.strip(Utils.setColors(titleName.toLowerCase()));
        for (Title title : dc.getTitles()) {
            if (title.getId().equalsIgnoreCase(titleId)) return false;
        }
        Title title = new Title(titleId, Utils.setColors(titleName), description, category);
        dc.addTitle(title);
        return true;
    }

    /**
     * Constructs a new category with a name and a description.
     * @param dc Container with all data.
     * @param categoryName Name of the new category.
     * @param description Description of the new category.
     * @return True if the new category was created successfully.
     */
    public static boolean createCategory(DataContainer dc, String categoryName, String description) {
        String categoryId = Utils.strip(Utils.setColors(categoryName.toLowerCase()));
        for (Category category : dc.getCategories()) {
            if (category.getId().equalsIgnoreCase(categoryId)) return false;
        }
        Category category = new Category(categoryId, categoryName, description);
        dc.addCategory(category);
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
