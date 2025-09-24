package com.innowise.userservice.util;

public class CardNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Card not found";

    public CardNotFoundException() {
        super(MESSAGE);
    }
}
