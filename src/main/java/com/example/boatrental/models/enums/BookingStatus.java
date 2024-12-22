package com.example.boatrental.models.enums;

public enum BookingStatus {
    CONFIRMED(1),
    PENDING_CONFIRMATION(2),
    COMPLETED(3),
    CANCELLED(4);

    private int value;

    BookingStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
