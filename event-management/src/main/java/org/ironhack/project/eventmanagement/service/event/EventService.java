package org.ironhack.project.eventmanagement.service.event;

import org.ironhack.project.eventmanagement.dto.request.event.CreateEventRequest;
import org.ironhack.project.eventmanagement.dto.request.event.UpdateEventRequest;
import org.ironhack.project.eventmanagement.dto.response.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse create(CreateEventRequest request);
    EventResponse getById(Long id);
    List<EventResponse> getAllPublished();
    List<EventResponse> getAll(); // admin
    EventResponse update(Long id, UpdateEventRequest request);
    void delete(Long id);
    EventResponse publish(Long id);
}
