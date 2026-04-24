package org.ironhack.project.eventmanagement.service.ticket.impl;

import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.repository.TicketCategoryRepository;
import org.ironhack.project.eventmanagement.service.ticket.TicketCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;

    public TicketCategoryServiceImpl(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    @Override
    public TicketCategory getById(Long id) {
        return ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket category not found"));
    }

    @Override
    public TicketCategory create(CreateTicketCategoryRequest request) {

        TicketCategory category = new TicketCategory();

        category.setName(request.getName());
        category.setPrice(request.getPrice());
        category.setQuantity(request.getQuantity());

        return ticketCategoryRepository.save(category);
    }

    @Override
    public List<TicketCategory> getAll() {
        return ticketCategoryRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        ticketCategoryRepository.deleteById(id);
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
    public void decreaseQuantity(Long id, int quantity) {
        TicketCategory category = getById(id);

        if (category.getQuantity() == null) {
            throw new BadRequestException("Ticket category quantity is not set");
        }
        category.setQuantity(category.getQuantity() - quantity);

        ticketCategoryRepository.save(category);
    }
}
