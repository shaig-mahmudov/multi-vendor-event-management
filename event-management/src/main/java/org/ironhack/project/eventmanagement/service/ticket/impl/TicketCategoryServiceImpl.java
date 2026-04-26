package org.ironhack.project.eventmanagement.service.ticket.impl;

import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.ironhack.project.eventmanagement.repository.EventRepository;
import org.ironhack.project.eventmanagement.repository.TicketCategoryRepository;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;
    private final EventRepository eventRepository;

    public TicketCategoryServiceImpl(TicketCategoryRepository ticketCategoryRepository,
                                     EventRepository eventRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public TicketCategory getById(Long id) {
        return ticketCategoryRepository.findById(id)
                .filter(TicketCategory::isActive)
                .orElseThrow(() -> new RuntimeException("TicketCategory not found"));
    }

    @Override
    @Transactional
    public TicketCategory create(CreateTicketCategoryRequest request) {

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        TicketCategory category = new TicketCategory();
        category.setName(request.getName());
        category.setPrice(request.getPrice());
        category.setQuantity(request.getQuantity());
        category.setEvent(event);
        category.setCreatedAt(LocalDateTime.now());

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
    public void delete(Long id) {
        TicketCategory category = getById(id);
        category.setActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        ticketCategoryRepository.save(category);
    }

    @Override
    public void validateAvailability(Long id, int quantity) {
        TicketCategory category = getById(id);

        if (category.getQuantity() < quantity) {
            throw new RuntimeException("Not enough tickets available");
        }
    }

    @Override
    @Transactional
    public void decreaseQuantity(Long id, int quantity) {
        TicketCategory category = getById(id);

        if (category.getQuantity() < quantity) {
            throw new RuntimeException("Not enough tickets");
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
}