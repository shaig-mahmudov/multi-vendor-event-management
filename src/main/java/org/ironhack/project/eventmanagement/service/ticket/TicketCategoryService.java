package org.ironhack.project.eventmanagement.service.ticket;

import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.TicketCategoryResponse;
import org.ironhack.project.eventmanagement.entity.TicketCategory;

import java.util.List;

public interface TicketCategoryService {

    TicketCategory create(Long eventId, CreateTicketCategoryRequest request);

    List<TicketCategory> getByEventId(Long eventId);

    List<TicketCategory> getAll();

    void delete(Long eventId, Long id);

    void validateAvailability(Long id, int quantity);

    void decreaseQuantity(Long id, int quantity);

    void increaseQuantity(Long id, int quantity);
}