package com.example.boatrental.services.impl;

import com.example.boatrental.dtos.BookingDto;
import com.example.boatrental.models.entities.Booking;
import com.example.boatrental.repositories.BoatRepository;
import com.example.boatrental.repositories.BookingRepository;
import com.example.boatrental.repositories.UserRepository;
import com.example.boatrental.rabbitmq.senders.BookingMessageSender;
import com.example.boatrental.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingMessageSender bookingMessageSender;

    @Value("bookingStatusQueue")
    private String bookingQueue;

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        var boat = boatRepository.findById(bookingDto.getBoatId())
                .orElseThrow(() -> new IllegalArgumentException("Лодка не найдена"));
        Booking savedBooking = bookingRepository.save(booking);
        String jsonMessage = String.format(
                "{\"bookingId\": \"%s\", \"boatId\": \"%s\", \"status\": \"%s\"}",
                savedBooking.getId(),
                boat.getId(),
                savedBooking.getStatus().toString()
        );
        bookingMessageSender.sendBookingMessage(jsonMessage);
        return modelMapper.map(savedBooking, BookingDto.class);
    }

    @Override
    public Optional<BookingDto> findBookingById(UUID id) {
        var bookingOptional = bookingRepository.findById(id);
        bookingOptional.ifPresent(booking -> {
            String message = String.format("Booking %s sent successfully.", booking.getId());
            bookingMessageSender.sendBookingMessage(message);
        });
        return bookingOptional.map(booking -> modelMapper.map(booking, BookingDto.class));
    }
    @Override
    public List<BookingDto> findBookingsByUserId(UUID userId) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        bookingMessageSender.sendBookingMessage(
                String.format("Sent bookings for user %s: %d records.", userId, bookings.size())
        );
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findBookingsByBoatId(UUID boatId) {
        List<Booking> bookings = bookingRepository.findAllByBoatId(boatId);
        bookingMessageSender.sendBookingMessage(
                String.format("Sent bookings for boat %s: %d records.", boatId, bookings.size())
        );
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        bookingMessageSender.sendBookingMessage(
                String.format("Sent all bookings: %d records.", bookings.size())
        );
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingDto> updateBooking(UUID id, BookingDto updatedBooking) {
        return bookingRepository.findById(id).map(booking -> {
            var user = userRepository.findById(updatedBooking.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
            var boat = boatRepository.findById(updatedBooking.getBoatId())
                    .orElseThrow(() -> new IllegalArgumentException("Лодка не найдена"));
            booking.setUser(user);
            booking.setBoat(boat);
            booking.setStartTime(updatedBooking.getStartTime());
            booking.setEndTime(updatedBooking.getEndTime());
            booking.setStatus(updatedBooking.getStatus());
            booking.setTotalPrice(updatedBooking.getTotalPrice());
            booking.setCreatedAt(updatedBooking.getCreatedAt());
            booking.setUpdatedAt(LocalDateTime.now());

            bookingMessageSender.sendBookingMessage("Booking " + updatedBooking.getId() + " status updated: " + updatedBooking.getStatus());

            return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
        });
    }

    @Override
    public void cancelBooking(UUID id) {
        bookingMessageSender.sendBookingMessage("Booking " + id + " deleted: ");

        bookingRepository.findById(id).ifPresent(bookingRepository::delete);
    }
}
