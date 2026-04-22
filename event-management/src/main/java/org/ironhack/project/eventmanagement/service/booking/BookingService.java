package org.ironhack.project.eventmanagement.service.booking;

import org.ironhack.project.eventmanagement.dto.request.booking.CreateBookingRequest;
import org.ironhack.project.eventmanagement.entity.Booking;

public interface BookingService {

    Booking createBooking(CreateBookingRequest request);

    Booking getById(Long id);

    void cancelBooking(Long bookingId);
}
