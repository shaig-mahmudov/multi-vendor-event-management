package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.response.TicketCategoryResponse;
import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.springframework.stereotype.Component;

@Component
public class TicketCategoryMapper {

    public TicketCategoryResponse toResponse(TicketCategory category) {

        TicketCategoryResponse response = new TicketCategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setPrice(category.getPrice());
        response.setQuantity(category.getQuantity());

        if (category.getEvent() != null) {
            response.setEventId(category.getEvent().getId());
            response.setEventName(category.getEvent().getTitle());
        }

        return response;
    }
}