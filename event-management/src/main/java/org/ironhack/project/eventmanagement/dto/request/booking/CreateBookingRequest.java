package org.ironhack.project.eventmanagement.dto.request.booking;

import java.util.List;

public class CreateBookingRequest {
    private List<BookingItemRequest> items;

    public List<BookingItemRequest> getItems() {
        return items;
    }

    public void setItems(List<BookingItemRequest> items) {
        this.items = items;
    }
}
