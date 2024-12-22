package com.example.boatrental.datafetchers.records;

import com.example.boatrental.models.enums.BoatStatus;
import com.example.boatrental.models.enums.BoatType;

import java.time.LocalDate;

public record SubmittedBoat(String name, String description, BoatType type, int capacity, BoatStatus status, double pricePerHour, LocalDate createdAt, String repairAt) {
}
