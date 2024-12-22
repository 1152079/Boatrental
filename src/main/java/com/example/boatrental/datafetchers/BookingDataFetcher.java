package com.example.boatrental.datafetchers;

import com.example.boatrental.datafetchers.records.SubmittedBooking;
import com.example.boatrental.dtos.BookingDto;
import com.example.boatrental.services.BookingService;
import com.netflix.graphql.dgs.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DgsComponent
public class BookingDataFetcher {

    private final BookingService bookingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookingDataFetcher(BookingService bookingService, ModelMapper modelMapper) {
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    @DgsQuery
    public List<BookingDto> allBookings() {
        return bookingService.findAllBookings();
    }

    @DgsQuery
    public BookingDto findBooking(@InputArgument UUID id) {
        return bookingService.findBookingById(id).orElse(null);
    }

    @DgsMutation
    public BookingDto registerBooking(@InputArgument SubmittedBooking input) {
        BookingDto newBooking = new BookingDto();
        newBooking.setUserId(input.userId());
        newBooking.setBoatId(input.boatId());
        newBooking.setStartTime(LocalDateTime.now());
        newBooking.setEndTime(LocalDateTime.now().plusHours(5));
        newBooking.setStatus(input.status());
        newBooking.setTotalPrice(input.totalPrice());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setUpdatedAt(LocalDateTime.now());
        return bookingService.createBooking(newBooking);
    }

    @DgsMutation
    public BookingDto updateBooking(@InputArgument UUID id, @InputArgument SubmittedBooking input) {
        BookingDto updateBooking = modelMapper.map(bookingService.findBookingById(id), BookingDto.class);
        updateBooking.setUserId(input.userId());
        updateBooking.setBoatId(input.boatId());
        updateBooking.setEndTime(LocalDateTime.now());
        updateBooking.setStatus(input.status());
        updateBooking.setTotalPrice(input.totalPrice());
        updateBooking.setUpdatedAt(LocalDateTime.now());
        return bookingService.updateBooking(id, updateBooking).orElse(null);
    }

    @DgsMutation
    public String deleteBooking(@InputArgument UUID id) {
        bookingService.cancelBooking(id);
        return "Бронирование с номером " + id + " было удалено";
    }
}
