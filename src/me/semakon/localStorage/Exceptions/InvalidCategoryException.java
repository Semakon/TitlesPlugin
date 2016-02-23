package me.semakon.localStorage.Exceptions;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class InvalidCategoryException extends Exception {

    public InvalidCategoryException() {
        super("No valid category was found.");
    }

}
