package com.mycompany.exceptions;

public class SeveralIdsFoundException extends RuntimeException {
    public SeveralIdsFoundException(String message) {
        super(message);
    }
}
