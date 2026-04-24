package org.ironhack.project.eventmanagement.dto.request.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateBookingRequest {

    @Valid
    @NotEmpty(message = "items cannot be empty")
    private List<BookingItemRequest> items;

    public List<BookingItemRequest> getItems() {
        return items;
    }

    public void setItems(List<BookingItemRequest> items) {
        this.items = items;
    }
}
