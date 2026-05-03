package org.ironhack.project.eventmanagement.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class BookingResponse {

    private Long id;
    private BigDecimal totalPrice;
    private String status;

    private List<BookingItemResponse> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BookingItemResponse> getItems() {
        return items;
    }

    public void setItems(List<BookingItemResponse> items) {
        this.items = items;
    }
}
