package com.srots.shared.exceptions;

public class SrotsException extends RuntimeException {

    public SrotsException(String message) {
        super(message);
    }

    public SrotsException(String message, Throwable cause) {
        super(message, cause);
    }
}
