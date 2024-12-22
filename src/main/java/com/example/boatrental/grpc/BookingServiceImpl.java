package com.example.boatrental.grpc;

import com.example.boatrental.*;
import com.example.boatrental.models.entities.Boat;
import com.example.boatrental.models.entities.Booking;
import com.example.boatrental.models.enums.BookingStatus;
import com.example.boatrental.models.entities.User;
import com.example.boatrental.repositories.BoatRepository;
import com.example.boatrental.repositories.BookingRepository;
import com.example.boatrental.repositories.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("bookingServiceImplGrpc")
public class BookingServiceImpl extends BookingServiceGrpc.BookingServiceImplBase {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final BoatRepository boatRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, BoatRepository boatRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.boatRepository = boatRepository;
    }

    @Override
    public void getBooking(BookingRequest request, StreamObserver<BookingResponse> responseObserver) {
        Optional<Booking> bookingOpt = bookingRepository.findById(UUID.fromString(request.getId()));
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();

            BookingResponse response = BookingResponse.newBuilder()
                    .setId(booking.getId().toString())
                    .setUserId(booking.getUser().toString())
                    .setBoatId(booking.getBoat().toString())
                    .setStartTime(booking.getStartTime().format(formatter))
                    .setEndTime(booking.getEndTime().format(formatter))
                    .setStatus(booking.getStatus().name())
                    .setTotalPrice(booking.getTotalPrice().doubleValue())
                    .setCreatedAt(booking.getCreatedAt().format(formatter))
                    .setUpdatedAt(booking.getUpdatedAt().format(formatter))
                    .build();

            responseObserver.onNext(response);
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Бронирование с ID " + request.getId() + " не найдено")
                    .asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getAllBookings(EmptyRequest request, StreamObserver<BookingListResponse> responseObserver) {
        List<Booking> bookings = bookingRepository.findAll();

        List<BookingResponse> bookingResponses = bookings.stream()
                .map(booking -> BookingResponse.newBuilder()
                        .setId(booking.getId().toString())
                        .setUserId(booking.getUser().toString())
                        .setBoatId(booking.getBoat().toString())
                        .setStartTime(booking.getStartTime().format(formatter))
                        .setEndTime(booking.getEndTime().format(formatter))
                        .setStatus(booking.getStatus().name())
                        .setTotalPrice(booking.getTotalPrice().doubleValue())
                        .setCreatedAt(booking.getCreatedAt().format(formatter))
                        .setUpdatedAt(booking.getUpdatedAt().format(formatter))
                        .build())
                .collect(Collectors.toList());

        BookingListResponse response = BookingListResponse.newBuilder()
                .addAllBookings(bookingResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addBooking(BookingCheckRequest request, StreamObserver<BookingCheckResponse> responseObserver) {
        String userId = request.getUser();
        String boatId = request.getBoat();

        Optional<User> userOpt = userRepository.findById(UUID.fromString(userId));
        Optional<Boat> boatOpt = boatRepository.findById(UUID.fromString(boatId));

        if (userOpt.isPresent() && boatOpt.isPresent()) {
            User user = userOpt.get();
            Boat boat = boatOpt.get();

            Booking newBooking = new Booking();
            newBooking.setStartTime(LocalDateTime.now());
            newBooking.setEndTime(LocalDateTime.now().plusHours(10));
            newBooking.setUser(user);
            newBooking.setBoat(boat);
            newBooking.setStatus(BookingStatus.PENDING_CONFIRMATION);
            newBooking.setTotalPrice(BigDecimal.valueOf(boat.getPricePerHour()));

            bookingRepository.save(newBooking);

            BookingCheckResponse response = BookingCheckResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Бронирование успешно")
                    .build();

            responseObserver.onNext(response);
        } else {
            BookingCheckResponse response = BookingCheckResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Пользователь или лодка не найдены")
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }
}