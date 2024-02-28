package com.dhiren.cloud.exceptions.custom;

public class ValidationBusinessException extends RuntimeException {

    private String message;

    public ValidationBusinessException(String message) {
        super(message);
        this.message = message;
    }
}
