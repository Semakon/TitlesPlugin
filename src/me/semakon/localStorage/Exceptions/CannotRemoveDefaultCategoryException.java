package me.semakon.localStorage.Exceptions;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class CannotRemoveDefaultCategoryException extends Exception {

    public CannotRemoveDefaultCategoryException() {
        super("Default category cannot be removed.");
    }

}
