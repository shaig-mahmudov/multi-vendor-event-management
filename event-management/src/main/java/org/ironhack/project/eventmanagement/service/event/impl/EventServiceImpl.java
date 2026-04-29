package org.ironhack.project.eventmanagement.service.event.impl;

import org.ironhack.project.eventmanagement.dto.request.event.CreateEventRequest;
import org.ironhack.project.eventmanagement.dto.request.event.UpdateEventRequest;
import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.ConflictException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.mapper.EventMapper;
import org.ironhack.project.eventmanagement.repository.*;
import org.ironhack.project.eventmanagement.service.event.EventService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper mapper;
    private final EventOrganizerRepository organizerRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, EventMapper mapper, EventOrganizerRepository organizerRepository, UserRepository userRepository, VendorRepository vendorRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.organizerRepository = organizerRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
    }

    // CREATE
    @Override
    public EventResponse create(CreateEventRequest request) {
        Vendor vendor = getCurrentVendor();

        Category category = null;

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        }

        Event event = mapper.toEntity(request, category);

        event.setStatus(EventStatus.DRAFT);
        event.setCreatedAt(LocalDateTime.now());

        Event saved = eventRepository.save(event);

        EventOrganizer organizer = new EventOrganizer();
        organizer.setEvent(saved);
        organizer.setVendor(vendor);
        organizer.setRole(OrganizerRole.MAIN);

        organizerRepository.save(organizer);

        return mapper.toResponse(saved);
    }

    // GET BY ID
    @Override
    public EventResponse getById(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        if(user.getRole().equals(Role.ADMIN)) {
            return mapper.toResponse(event);
        }

        if (event.getStatus() == EventStatus.PUBLISHED) {
            return mapper.toResponse(event);
        }

        Vendor vendor = vendorRepository.findByUserId(user.getId())
                .orElse(null);

        if (vendor != null) {
            boolean isOwner = organizerRepository
                    .existsByEventIdAndVendorIdAndRole(id, vendor.getId(), OrganizerRole.MAIN);

            if (isOwner) {
                return mapper.toResponse(event);
            }
        }

        throw new NotFoundException("Event not found");
    }

    // PUBLIC LISTING
    @Override
    public List<EventResponse> getAllPublished(Long categoryId,
                                               LocalDateTime fromDate,
                                               LocalDateTime toDate) {

        List<Event> events;

        if (fromDate != null && toDate != null) {
            if (categoryId != null) {
                events = eventRepository.findByCategoryIdAndStatusAndDateBetween(
                        categoryId, EventStatus.PUBLISHED, fromDate, toDate);
            } else {
                events = eventRepository.findByStatusAndDateBetween(
                        EventStatus.PUBLISHED, fromDate, toDate);
            }
        } else if (fromDate != null) {
            if (categoryId != null) {
                events = eventRepository.findByCategoryIdAndStatusAndDateAfter(
                        categoryId, EventStatus.PUBLISHED, fromDate);
            } else {
                events = eventRepository.findByStatusAndDateAfter(
                        EventStatus.PUBLISHED, fromDate);
            }
        } else if (toDate != null) {
            if (categoryId != null) {
                events = eventRepository.findByCategoryIdAndStatusAndDateBefore(
                        categoryId, EventStatus.PUBLISHED, toDate);
            } else {
                events = eventRepository.findByStatusAndDateBefore(
                        EventStatus.PUBLISHED, toDate);
            }
        } else {
            if (categoryId != null) {
                events = eventRepository.findByCategoryIdAndStatus(
                        categoryId, EventStatus.PUBLISHED);
            } else {
                events = eventRepository.findByStatus(EventStatus.PUBLISHED);
            }
        }

        return events.stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ADMIN LIST
    @Override
    public List<EventResponse> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // UPDATE (FIXED)
    @Override
    public EventResponse update(Long id, UpdateEventRequest request) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)) {
            Vendor vendor = getCurrentVendor();

            checkOwnership(id, vendor.getId());

            if(event.getStatus() == EventStatus.PUBLISHED){
                throw new ConflictException("Cannot update published event");
            }
        }

        Category category = null;

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getDate() != null) {
            event.setDate(request.getDate());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getImageUrl() != null) {
            event.setImageUrl(request.getImageUrl());
        }

        if (category != null) {
            event.setCategory(category);
        }

        event.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(eventRepository.save(event));
    }

    // DELETE
    @Override
    public void delete(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user =  getCurrentUser();

        if (!user.getRole().equals(Role.ADMIN)) {
            Vendor vendor = getCurrentVendor();
            checkOwnership(id, vendor.getId());
        }

        if(event.getStatus() == EventStatus.CANCELLED){
            throw new ConflictException("Event already deleted(cancelled)!");
        }

        event.setStatus(EventStatus.CANCELLED);
        event.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
    }

    // PUBLISH
    @Override
    public EventResponse publish(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)) {
            Vendor vendor = getCurrentVendor();
            checkOwnership(id, vendor.getId());
        }

        validateForPublish(event);

        event.setStatus(EventStatus.PUBLISHED);
        event.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(eventRepository.save(event));
    }

    // VALIDATION
    private void validateForPublish(Event event) {

        List<String> errors = new ArrayList<>();

        if (event.getTitle() == null || event.getTitle().isBlank()) {
            errors.add("Title is required");
        }

        if (event.getDescription() == null || event.getDescription().isBlank()) {
            errors.add("Description is required");
        }

        if (event.getDate() == null || event.getDate().isBefore(LocalDateTime.now())) {
            errors.add("Date must be in the future");
        }

        if (event.getLocation() == null || event.getLocation().isBlank()) {
            errors.add("Location is required");
        }

        if (event.getCategory() == null) {
            errors.add("Category is required");
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException("To publish event fill all required data: " + String.join(", ", errors));
        }
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
}

