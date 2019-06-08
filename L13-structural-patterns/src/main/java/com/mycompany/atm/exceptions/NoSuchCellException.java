package com.mycompany.atm.exceptions;

/**
 * Thrown when there is no cell to store a banknote.
 */
public class NoSuchCellException extends RuntimeException {

    public NoSuchCellException(String message) {
        super(message);
    }
}
