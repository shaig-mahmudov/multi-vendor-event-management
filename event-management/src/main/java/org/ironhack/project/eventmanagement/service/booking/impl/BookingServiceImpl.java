package org.ironhack.project.eventmanagement.service.booking.impl;

import org.ironhack.project.eventmanagement.dto.request.booking.BookingItemRequest;
import org.ironhack.project.eventmanagement.dto.request.booking.CreateBookingRequest;
import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.repository.BookingRepository;
import org.ironhack.project.eventmanagement.service.booking.BookingService;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TicketCategoryService ticketCategoryService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              TicketCategoryService ticketCategoryService) {
        this.bookingRepository = bookingRepository;
        this.ticketCategoryService = ticketCategoryService;
    }

    @Transactional
    @Override
    public Booking createBooking(CreateBookingRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Booking items cannot be empty");
        }

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());


        User user = new User();
        user.setId(1L);
        booking.setUser(user);

        List<BookingItem> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (BookingItemRequest reqItem : request.getItems()) {

            TicketCategory category =
                    ticketCategoryService.getById(reqItem.getTicketCategoryId());

            ticketCategoryService.validateAvailability(
                    category.getId(),
                    reqItem.getQuantity()
            );

            BookingItem item = new BookingItem();
            item.setBooking(booking);
            item.setTicketCategory(category);
            item.setQuantity(reqItem.getQuantity());
            item.setPrice(category.getPrice());

            items.add(item);

            totalPrice = totalPrice.add(
                    category.getPrice().multiply(
                            BigDecimal.valueOf(reqItem.getQuantity())
                    )
            );

            ticketCategoryService.decreaseQuantity(
                    category.getId(),
                    reqItem.getQuantity()
            );
        }

        booking.setItems(items);
        booking.setTotalPrice(totalPrice);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public void cancelBooking(Long bookingId) {

        Booking booking = getById(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        for (BookingItem item : booking.getItems()) {
            ticketCategoryService.decreaseQuantity(
                    item.getTicketCategory().getId(),
                    -item.getQuantity()
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }
}