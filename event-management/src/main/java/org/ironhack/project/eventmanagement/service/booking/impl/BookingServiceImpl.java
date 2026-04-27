package org.ironhack.project.eventmanagement.service.booking.impl;

import org.ironhack.project.eventmanagement.dto.request.booking.BookingItemRequest;
import org.ironhack.project.eventmanagement.dto.request.booking.CreateBookingRequest;
import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.repository.BookingRepository;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.service.booking.BookingService;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              TicketCategoryService ticketCategoryService,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketCategoryService = ticketCategoryService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Booking createBooking(CreateBookingRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Booking items cannot be empty");
        }

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        booking.setUser(requireCurrentUser());

        List<BookingItem> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (BookingItemRequest reqItem : request.getItems()) {
            if (reqItem.getTicketCategoryId() == null) {
                throw new BadRequestException("ticketCategoryId is required");
            }
            if (reqItem.getQuantity() == null || reqItem.getQuantity() <= 0) {
                throw new BadRequestException("quantity must be greater than 0");
            }

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
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    @Override
    public void cancelBooking(Long bookingId) {

        Booking booking = getById(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
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

    private User requireCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}