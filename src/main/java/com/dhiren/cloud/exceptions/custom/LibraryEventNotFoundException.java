package com.dhiren.cloud.exceptions.custom;

public class LibraryEventNotFoundException extends RuntimeException {

    private String message;

    public LibraryEventNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
