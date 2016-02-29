package me.semakon.localStorage.Exceptions;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class InvalidTitleRuntimeException extends RuntimeException {

    public InvalidTitleRuntimeException() {
        super("No valid title was found.");
    }

}
