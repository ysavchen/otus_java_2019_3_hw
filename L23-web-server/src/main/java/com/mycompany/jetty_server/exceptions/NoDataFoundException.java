package com.mycompany.jetty_server.exceptions;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException() {
    }

    public NoDataFoundException(String message) {
        super(message);
    }
}
