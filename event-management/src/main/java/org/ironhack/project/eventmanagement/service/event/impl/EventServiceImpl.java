package org.ironhack.project.eventmanagement.service.event.impl;

import org.ironhack.project.eventmanagement.dto.request.event.CreateEventRequest;
import org.ironhack.project.eventmanagement.dto.request.event.UpdateEventRequest;
import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.entity.Category;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.EventStatus;
import org.ironhack.project.eventmanagement.mapper.EventMapper;
import org.ironhack.project.eventmanagement.repository.CategoryRepository;
import org.ironhack.project.eventmanagement.repository.EventRepository;
import org.ironhack.project.eventmanagement.service.event.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private CategoryRepository categoryRepository;
    private EventMapper mapper;

    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, EventMapper mapper) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    // create
    @Override
    public EventResponse create(CreateEventRequest request){
        Category category = null;
        if(request.getCategoryId() != null){
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }
        Event event = mapper.toEntity(request,category);

        event.setStatus(EventStatus.DRAFT);
        event.setCreatedAt(LocalDateTime.now());

        Event saved =  eventRepository.save(event);
        return mapper.toResponse(saved);
    }

    // get by id
    @Override
    public EventResponse getById(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapper.toResponse(event);
    }

    // public listing
    @Override
    public List<EventResponse> getAllPublished(){
        return eventRepository.findByStatus(EventStatus.PUBLISHED)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // admin listing
    @Override
    public List<EventResponse> getAll(){
        return eventRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // update
    @Override
    public EventResponse update(Long id, UpdateEventRequest request){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Category category = null;

        if(request.getCategoryId() != null){
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }
        mapper.updateEntity(request,event,category);

        return mapper.toResponse(eventRepository.save(event));
    }

    // delete
    @Override
    public void delete(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        eventRepository.delete(event);
    }

    // publish
    @Override
    public EventResponse publish(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        validateForPublish(event);

        event.setStatus(EventStatus.PUBLISHED);
        event.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(eventRepository.save(event));
    }

    // helper methods

    //validate for publish
    private void validateForPublish(Event event){
        List<String> errors = new ArrayList<>();

        if(event.getTitle() == null || event.getTitle().isEmpty()){
            errors.add("Title is required");
        }

        if(event.getTitle() != null &&
                (event.getTitle().length() < 3 || event.getTitle().length() > 50)){
            errors.add("Title length must be between 3 and 50 characters");
        }

        if(event.getDescription() == null || event.getDescription().isEmpty()){
            errors.add("Description is required");
        }

        if(event.getDescription() != null &&
                event.getDescription().length() > 255){
            errors.add("Description length must be less than 256 characters");
        }

        if(event.getDate() == null || event.getDate().isBefore(LocalDateTime.now())){
            errors.add("Date is required and must be in the future");
        }

        if(event.getLocation() == null || event.getLocation().isEmpty()){
            errors.add("Location is required");
        }

        if(event.getCategory() == null){
            errors.add("Category is required");
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join(", ", errors));
        }
    }
}
