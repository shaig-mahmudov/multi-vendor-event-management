package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.TicketCategoryResponse;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.ironhack.project.eventmanagement.mapper.TicketCategoryMapper;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;
    private final TicketCategoryMapper ticketCategoryMapper;

    public TicketCategoryController(TicketCategoryService ticketCategoryService,
                                    TicketCategoryMapper ticketCategoryMapper) {
        this.ticketCategoryService = ticketCategoryService;
        this.ticketCategoryMapper = ticketCategoryMapper;
    }

    @PostMapping("/events/{eventId}/ticket-categories")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public TicketCategoryResponse create(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateTicketCategoryRequest request
    ) {
        TicketCategory saved = ticketCategoryService.create(eventId, request);
        return ticketCategoryMapper.toResponse(saved);
    }

    @GetMapping("/events/{eventId}/ticket-categories")
    public List<TicketCategoryResponse> getByEvent(@PathVariable Long eventId) {
        return ticketCategoryService.getByEventId(eventId)
                .stream()
                .map(ticketCategoryMapper::toResponse)
                .toList();
    }

    @GetMapping("/ticket-categories/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TicketCategoryResponse> getAll() {
        return ticketCategoryService.getAll()
                .stream()
                .map(ticketCategoryMapper::toResponse)
                .toList();
    }

    @DeleteMapping("/events/{eventId}/ticket-categories/{id}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public void delete(@PathVariable Long eventId,
                       @PathVariable Long id) {
        ticketCategoryService.delete(eventId, id);
    }
}