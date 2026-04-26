package org.ironhack.project.eventmanagement.dto.request.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateBookingRequest {

    @NotNull(message = "Items cannot be null")
    @NotEmpty(message = "Booking must contain at least one item")
    @Valid
    private List<BookingItemRequest> items;

    public List<BookingItemRequest> getItems() {
        return items;
    }

    public void setItems(List<BookingItemRequest> items) {
        this.items = items;
    }
}
