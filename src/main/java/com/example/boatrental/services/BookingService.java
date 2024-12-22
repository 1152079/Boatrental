package com.example.boatrental.services;

import com.example.boatrental.dtos.BookingDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {
    BookingDto createBooking(BookingDto booking);
    Optional<BookingDto> findBookingById(UUID id);
    List<BookingDto> findBookingsByUserId(UUID userId);
    List<BookingDto> findBookingsByBoatId(UUID boatId);
    List<BookingDto> findAllBookings();
    Optional<BookingDto> updateBooking(UUID id, BookingDto updatedBooking);
    void cancelBooking(UUID id);
}
