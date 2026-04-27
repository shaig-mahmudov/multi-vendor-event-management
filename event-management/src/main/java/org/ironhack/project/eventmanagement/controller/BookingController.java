package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.booking.CreateBookingRequest;
import org.ironhack.project.eventmanagement.dto.response.BookingResponse;
import org.ironhack.project.eventmanagement.entity.Booking;
import org.ironhack.project.eventmanagement.mapper.BookingMapper;
import org.ironhack.project.eventmanagement.service.booking.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    public BookingController(BookingService bookingService,
                             BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @PostMapping
    public BookingResponse createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        Booking booking = bookingService.createBooking(request);
        return bookingMapper.toResponse(booking);
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {

        Booking booking = bookingService.getById(id);
        return bookingMapper.toResponse(booking);
    }

    @DeleteMapping("/{id}")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}