package me.semakon.localStorage.Exceptions;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class InvalidTitleException extends Exception {

    public InvalidTitleException() {
        super("The title is invalid."); //TODO: elaborate
    }

    public InvalidTitleException(String msg) {
        super(msg);
    }

}
