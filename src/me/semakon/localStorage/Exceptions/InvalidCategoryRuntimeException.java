package me.semakon.localStorage.Exceptions;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class InvalidCategoryRuntimeException extends RuntimeException {

    public InvalidCategoryRuntimeException() {
        super("No valid category was found.");
    }

}
