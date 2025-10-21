package com.innowise.userservice.util;

public class SecretWordAccessDeniedException extends RuntimeException {
    private static final String MESSAGE = "Forbidden";

    public SecretWordAccessDeniedException() {
        super(MESSAGE);
    }
}
