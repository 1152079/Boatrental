package com.example.boatrental.controllers.exception;

public record StatusResponse(
        String status,
        String message
) {}