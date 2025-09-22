package com.innowise.userservice.util;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException() {
        super("Card wasn't found");
    }
}
