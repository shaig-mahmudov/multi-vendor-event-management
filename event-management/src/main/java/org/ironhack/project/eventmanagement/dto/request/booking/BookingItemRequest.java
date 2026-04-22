package org.ironhack.project.eventmanagement.dto.request.booking;

public class BookingItemRequest {

    private Long ticketCategoryId;
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
