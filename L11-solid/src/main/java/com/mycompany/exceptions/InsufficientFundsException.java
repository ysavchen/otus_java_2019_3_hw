package com.mycompany.exceptions;

/**
 * Thrown when an account does not have enough money to be issued by ATM.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
