package org.ironhack.project.eventmanagement.dto.request.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingItemRequest {

    @NotNull(message = "ticketCategoryId is required")
    private Long ticketCategoryId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;

    public Long getTicketCategoryId() {
        return ticketCategoryId;
    }

    public void setTicketCategoryId(Long ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}