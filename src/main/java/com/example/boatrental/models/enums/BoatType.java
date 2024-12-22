package com.example.boatrental.models.enums;

public enum BoatType {
    MOTORBOAT(1),
    SAILBOAT(2),
    YACHT(3),
    KAYAK(4),
    CANOE(5);

    private int value;

    BoatType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}


