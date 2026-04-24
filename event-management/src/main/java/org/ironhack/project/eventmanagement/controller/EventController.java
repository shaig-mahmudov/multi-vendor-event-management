package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.event.CreateEventRequest;
import org.ironhack.project.eventmanagement.dto.request.event.UpdateEventRequest;
import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.service.event.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(@RequestBody @Valid CreateEventRequest eventRequest){
        return eventService.create(eventRequest);
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id){
        return eventService.getById(id);
    }

    // public listing
    @GetMapping
    public List<EventResponse> getAllPublished(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)LocalDateTime fromDate,
            @RequestParam(required = false)LocalDateTime toDate){
        return eventService.getAllPublished(categoryId, fromDate, toDate);
    }

    // admin listing
    @GetMapping("/all")
    public List<EventResponse> getAll(){
        return eventService.getAll();
    }

    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id,@RequestBody @Valid UpdateEventRequest request){
        return  eventService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        eventService.delete(id);
    }

    @PatchMapping("/{id}/publish")
    public EventResponse publish(@PathVariable Long id){
        return eventService.publish(id);
    }
}
