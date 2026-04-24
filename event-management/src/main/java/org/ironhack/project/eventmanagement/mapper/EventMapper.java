package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.request.event.CreateEventRequest;
import org.ironhack.project.eventmanagement.dto.request.event.UpdateEventRequest;
import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.entity.Category;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.EventStatus;
import org.ironhack.project.eventmanagement.repository.CategoryRepository;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;

@Component
public class EventMapper {
    // for create
    public Event toEntity(CreateEventRequest request, Category category) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());
        event.setLocation(request.getLocation());
        event.setImageUrl(request.getImageUrl());
        event.setCategory(category);
        event.setStatus(EventStatus.DRAFT);
        event.setCreatedAt(LocalDateTime.now());
        return event;
    }

    // for update
    public void updateEntity(UpdateEventRequest request, Event event, Category category) {
        if(request.getTitle() != null){
            event.setTitle(request.getTitle());
        }

        if(request.getDescription() != null){
            event.setDescription(request.getDescription());
        }

        if (request.getDate() != null){
            event.setDate(request.getDate());
        }

        if(request.getLocation() != null){
            event.setLocation(request.getLocation());
        }

        if(request.getImageUrl() != null){
            event.setImageUrl(request.getImageUrl());
        }

        if(category != null){
            event.setCategory(category);
        }

        event.setUpdatedAt(LocalDateTime.now());
    }

    // for response
    public EventResponse toResponse(Event event){
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getImageUrl(),
                event.getStatus(),
                event.getCategory() != null ? event.getCategory().getId() : null,
                event.getCategory() != null ? event.getCategory().getName() : null,
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
