package com.example.boatrental.controllers.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super("Not found: " + message);
    }

    public NotFoundException(UUID id) {
        super("Not found: " + id);
    }
}