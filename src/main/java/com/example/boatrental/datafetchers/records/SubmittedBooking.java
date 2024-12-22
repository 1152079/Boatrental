package com.example.boatrental.datafetchers.records;

import com.example.boatrental.models.enums.BookingStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record SubmittedBooking(UUID userId, UUID boatId, String startTime, String endTime, BookingStatus status, BigDecimal totalPrice) {
}
