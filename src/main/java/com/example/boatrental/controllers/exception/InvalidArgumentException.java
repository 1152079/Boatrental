package com.example.boatrental.controllers.exception;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super("Invalid argument: " + message);
    }
}

