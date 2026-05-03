package org.ironhack.project.eventmanagement.service.ticket.impl;

import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.ConflictException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.repository.*;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;
    private final EventRepository eventRepository;
    private final EventOrganizerRepository organizerRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    public TicketCategoryServiceImpl(TicketCategoryRepository ticketCategoryRepository,
                                     EventRepository eventRepository,
                                     EventOrganizerRepository organizerRepository,
                                     UserRepository userRepository,
                                     VendorRepository vendorRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<TicketCategory> getByEventId(Long eventId) {
        List<TicketCategory> categories = ticketCategoryRepository.findByEventId(eventId);

        User user = getCurrentUser();
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        boolean isOwner = !isAdmin &&
                vendorRepository.findByUserId(user.getId())
                        .map(v -> organizerRepository
                                .existsByEventIdAndVendorIdAndRole(eventId, v.getId(), OrganizerRole.MAIN))
                        .orElse(false);

        boolean isAdminOrOwner = isAdmin || isOwner;

        return categories.stream()
                .filter(tc -> tc.isActive() || isAdminOrOwner)
                .toList();
    }

    @Override
    @Transactional
    public TicketCategory create(Long eventId,CreateTicketCategoryRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)){
            Vendor vendor = getCurrentVendor();
            checkOwnership(eventId,vendor.getId());
        }

        if(event.getStatus() == EventStatus.CANCELLED){
            throw new ConflictException("Cannot add tickets to cancelled event");
        }

        TicketCategory category = new TicketCategory();
        category.setName(request.getName());
        category.setPrice(request.getPrice());
        category.setQuantity(request.getQuantity());
        category.setEvent(event);
        category.setCreatedAt(LocalDateTime.now());
        category.setActive(true);

        return ticketCategoryRepository.save(category);
    }

    @Override
    public List<TicketCategory> getAll() {
        return ticketCategoryRepository.findAll()
                .stream()
                .filter(TicketCategory::isActive)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long eventId,Long id) {
        TicketCategory category = getById(id);

        if(!category.getEvent().getId().equals(eventId)){
            throw new BadRequestException("Category does not belong to this event");
        }

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)){
            Vendor vendor = getCurrentVendor();
            checkOwnership(eventId,vendor.getId());
        }

        category.setActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        ticketCategoryRepository.save(category);
    }

    @Override
    public void validateAvailability(Long id, int quantity) {
        TicketCategory category = getById(id);

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        if (category.getQuantity() == null || category.getQuantity() < quantity) {
            throw new BadRequestException("Not enough tickets available");
        }
    }

    @Override
    @Transactional
    public void decreaseQuantity(Long id, int quantity) {
        TicketCategory category = getById(id);

        if (category.getQuantity() == null) {
            throw new BadRequestException("Ticket category quantity is not set");
        }

        if (category.getQuantity() < quantity) {
            throw new BadRequestException("Not enough tickets");
        }

        category.setQuantity(category.getQuantity() - quantity);
        category.setUpdatedAt(LocalDateTime.now());

        ticketCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public void increaseQuantity(Long id, int quantity) {
        TicketCategory category = getById(id);

        category.setQuantity(category.getQuantity() + quantity);
        category.setUpdatedAt(LocalDateTime.now());

        ticketCategoryRepository.save(category);
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private Vendor getCurrentVendor() {
        User user = getCurrentUser();

        return vendorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("User is not a vendor"));
    }

    private void checkOwnership(Long eventId, Long vendorId) {
        boolean isOwner = organizerRepository
                .existsByEventIdAndVendorIdAndRole(eventId, vendorId, OrganizerRole.MAIN);

        if (!isOwner) {
            throw new UnauthorizedException("You are not owner of this event");
        }
    }

    private TicketCategory getById(Long id) {
        return ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket category not found"));
    }
}