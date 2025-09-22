package com.innowise.userservice.util;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User wasn't found");
    }
}
