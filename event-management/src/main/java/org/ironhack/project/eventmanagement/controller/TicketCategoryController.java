package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.TicketCategoryResponse;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.ironhack.project.eventmanagement.mapper.TicketCategoryMapper;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-categories")
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;
    private final TicketCategoryMapper ticketCategoryMapper;

    public TicketCategoryController(TicketCategoryService ticketCategoryService,
                                    TicketCategoryMapper ticketCategoryMapper) {
        this.ticketCategoryService = ticketCategoryService;
        this.ticketCategoryMapper = ticketCategoryMapper;
    }

    @PostMapping
    public TicketCategoryResponse create(
            @Valid @RequestBody CreateTicketCategoryRequest request
    ) {
        TicketCategory saved = ticketCategoryService.create(request);
        return ticketCategoryMapper.toResponse(saved);
    }

    @GetMapping("/{id}")
    public TicketCategoryResponse getById(@PathVariable Long id) {
        TicketCategory category = ticketCategoryService.getById(id);
        return ticketCategoryMapper.toResponse(category);
    }

    @GetMapping
    public List<TicketCategoryResponse> getAll() {
        return ticketCategoryService.getAll()
                .stream()
                .map(ticketCategoryMapper::toResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ticketCategoryService.delete(id);
    }
}