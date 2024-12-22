package com.example.boatrental.models.enums;

    public enum BoatStatus {
        AVAILABLE(1),
        BOOKED(2),
        UNDER_REPAIR(3);

        private int value;

        BoatStatus(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

        }