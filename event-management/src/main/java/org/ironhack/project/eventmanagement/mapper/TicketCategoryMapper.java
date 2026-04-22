package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.request.ticket.CreateTicketCategoryRequest;
import org.ironhack.project.eventmanagement.dto.response.TicketCategoryResponse;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.springframework.stereotype.Component;

@Component
public class TicketCategoryMapper {

    public TicketCategory toEntity(CreateTicketCategoryRequest request) {

        TicketCategory category = new TicketCategory();

        category.setName(request.getName());
        category.setPrice(request.getPrice());
        category.setQuantity(request.getQuantity());

        return category;
    }

    public TicketCategoryResponse toResponse(TicketCategory category) {

        TicketCategoryResponse response = new TicketCategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setPrice(category.getPrice());
        response.setQuantity(category.getQuantity());

        return response;
    }
}