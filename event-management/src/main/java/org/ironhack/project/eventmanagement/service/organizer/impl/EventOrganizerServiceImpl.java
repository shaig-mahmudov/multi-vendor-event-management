package org.ironhack.project.eventmanagement.service.organizer.impl;

import org.ironhack.project.eventmanagement.dto.request.organizer.AddOrganizerRequest;
import org.ironhack.project.eventmanagement.dto.response.OrganizerResponse;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.EventOrganizer;
import org.ironhack.project.eventmanagement.entity.Vendor;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.repository.EventOrganizerRepository;
import org.ironhack.project.eventmanagement.repository.EventRepository;
import org.ironhack.project.eventmanagement.repository.VendorRepository;
import org.ironhack.project.eventmanagement.service.organizer.EventOrganizerService;

import java.util.List;

public class EventOrganizerServiceImpl implements EventOrganizerService {
    private final EventOrganizerRepository organizerRepository;
    private final EventRepository eventRepository;
    private VendorRepository vendorRepository;

    public EventOrganizerServiceImpl(EventOrganizerRepository organizerRepository, EventRepository eventRepository, VendorRepository vendorRepository) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
        this.vendorRepository = vendorRepository;
    }

    // add organizer
    @Override
    public OrganizerResponse addOrganizer(Long eventId, AddOrganizerRequest request){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new NotFoundException("Vendor Not Found"));

        if(organizerRepository.existsByEventIdAndVendorId(eventId, vendor.getId())){
            throw new BadRequestException("Organizer already exists for this event");
        }

        EventOrganizer organizer = new EventOrganizer();
        organizer.setEvent(event);
        organizer.setVendor(vendor);
        organizer.setRole(request.getRole());

        EventOrganizer saved =  organizerRepository.save(organizer);

        return new OrganizerResponse(
                saved.getId(),
                vendor.getId(),
                vendor.getName(),
                saved.getRole()
        );
    }

    // get organizers
    @Override
    public List<OrganizerResponse> getOrganizers(Long eventId) {
        return organizerRepository.findByEventId(eventId)
                .stream()
                .map(o -> new OrganizerResponse(
                        o.getId(),
                        o.getVendor().getId(),
                        o.getVendor().getName(),
                        o.getRole()
                ))
                .toList();
    }

    // remove organizer
    @Override
    public void removeOrganizer(Long organizerId) {
        EventOrganizer organizer = organizerRepository.findById(organizerId)
                        .orElseThrow(() -> new NotFoundException("Organizer not found"));

        organizerRepository.delete(organizer);
    }
}
