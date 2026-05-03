package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.response.BookingItemResponse;
import org.ironhack.project.eventmanagement.dto.response.BookingResponse;
import org.ironhack.project.eventmanagement.entity.Booking;
import org.ironhack.project.eventmanagement.entity.BookingItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setId(booking.getId());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus().name());

        List<BookingItemResponse> items = booking.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        response.setItems(items);

        return response;
    }

    private BookingItemResponse toItemResponse(BookingItem item) {

        BookingItemResponse response = new BookingItemResponse();

        response.setTicketCategoryId(item.getTicketCategory().getId());
        response.setTicketCategoryName(item.getTicketCategory().getName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());

        return response;
    }
}
