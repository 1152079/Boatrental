package com.example.boatrental.controllers;

import com.example.boatrental.controllers.exception.NotFoundException;
import com.example.boatrental.dtos.BookingDto;
import com.example.boatrental.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ModelMapper modelMapper;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public BookingController(BookingService bookingService, ModelMapper modelMapper) {
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomResponse<BookingDto>>> all() {
        List<BookingDto> bookings = bookingService.findAllBookings();
        List<CustomResponse<BookingDto>> response = bookings.stream()
                .map(this::createBookingResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<BookingDto>> newBooking(@RequestBody BookingDto newBookingDto) {
        BookingDto booking = bookingService.createBooking(newBookingDto);
        CustomResponse<BookingDto> response = createBookingResponse(booking);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<BookingDto>> findBooking(@PathVariable UUID id) {
        BookingDto booking = bookingService.findBookingById(id).orElseThrow(() -> new NotFoundException(id));
        CustomResponse<BookingDto> response = createBookingResponse(booking);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<BookingDto>> updateBooking(@PathVariable UUID id, @RequestBody BookingDto bookingDto) {
        Optional<BookingDto> updatedBookingOptional = bookingService.updateBooking(id, bookingDto);
        if (updatedBookingOptional.isEmpty()) {
            throw new NotFoundException("Booking not found with id: " + id);
        }
        CustomResponse<BookingDto> response = createBookingResponse(updatedBookingOptional.get());
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable UUID id) {
        bookingService.cancelBooking(id);
        Map<String, Object> response = Map.of(
                "message", "Booking deleted successfully.",
                "links", List.of(
                        WebMvcLinkBuilder.linkTo(methodOn(BookingController.class).all()).withRel("all-bookings").getHref(),
                        WebMvcLinkBuilder.linkTo(methodOn(BookingController.class).newBooking(null)).withRel("create-new").getHref()
                )
        );

        return ResponseEntity.ok(response);
    }

    private CustomResponse<BookingDto> createBookingResponse(BookingDto booking) {
        EntityModel<BookingDto> resource = EntityModel.of(booking);
        addBookingLinks(resource, booking);
        addActions(resource, booking);

        return new CustomResponse<>(resource);
    }

    private void addBookingLinks(EntityModel<BookingDto> resource, BookingDto booking) {
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(BookingController.class).findBooking(booking.getId())).withSelfRel();
        Link allBookingsLink = WebMvcLinkBuilder.linkTo(methodOn(BookingController.class).all()).withRel("all-bookings");
        resource.add(selfLink);
        resource.add(allBookingsLink);
    }

    private void addActions(EntityModel<BookingDto> resource, BookingDto booking) {
        Link updateLink = WebMvcLinkBuilder.linkTo(methodOn(BookingController.class).updateBooking(booking.getId(), booking)).withRel("update");
        Link deleteLink = WebMvcLinkBuilder.linkTo(methodOn(BookingController.class).deleteBooking(booking.getId())).withRel("delete");
        resource.add(updateLink);
        resource.add(deleteLink);
    }
}
