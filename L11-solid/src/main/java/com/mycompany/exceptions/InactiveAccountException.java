package com.mycompany.exceptions;

/**
 * Thrown when an account is deactivated.
 */
public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException(String message){
        super(message);
    }
}
