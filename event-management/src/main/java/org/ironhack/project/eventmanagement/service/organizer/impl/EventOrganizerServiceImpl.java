package org.ironhack.project.eventmanagement.service.organizer.impl;

import org.ironhack.project.eventmanagement.dto.request.organizer.AddOrganizerRequest;
import org.ironhack.project.eventmanagement.dto.response.OrganizerResponse;
import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.ConflictException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.repository.EventOrganizerRepository;
import org.ironhack.project.eventmanagement.repository.EventRepository;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.repository.VendorRepository;
import org.ironhack.project.eventmanagement.service.organizer.EventOrganizerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventOrganizerServiceImpl implements EventOrganizerService {
    private final EventOrganizerRepository organizerRepository;
    private final EventRepository eventRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public EventOrganizerServiceImpl(EventOrganizerRepository organizerRepository,
                                     EventRepository eventRepository,
                                     VendorRepository vendorRepository,
                                     UserRepository userRepository) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    // add organizer
    @Override
    public OrganizerResponse addOrganizer(Long eventId, AddOrganizerRequest request){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)){
            // getting current vendor
            Vendor currentVendor = getCurrentVendor();
            checkOwnership(eventId, currentVendor.getId());
        }

        // getting target vendor
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new NotFoundException("Vendor Not Found"));

        if(organizerRepository.existsByEventIdAndVendorId(eventId, vendor.getId())){
            throw new ConflictException("Organizer already exists for this event");
        }

        if(request.getRole() == OrganizerRole.MAIN){
            throw new BadRequestException("Use transfer endpoint to assign MAIN role(current owner will lose access to event and become co-organizer)");
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
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)){
            return map(eventId);
        }

        if (event.getStatus() == EventStatus.PUBLISHED) {
            return map(eventId);
        }

        Vendor vendor = getCurrentVendor();
        checkOwnership(eventId, vendor.getId());

        return map(eventId);
    }

    // remove organizer
    @Override
    public void removeOrganizer(Long organizerId) {
        EventOrganizer organizer = organizerRepository.findById(organizerId)
                        .orElseThrow(() -> new NotFoundException("Organizer not found"));

        User user = getCurrentUser();

        if(!user.getRole().equals(Role.ADMIN)){
            Vendor currentVendor = getCurrentVendor();
            checkOwnership(organizer.getEvent().getId(), currentVendor.getId());
        }

        if (organizer.getRole() ==  OrganizerRole.MAIN) {
            throw new BadRequestException("Cannot remove MAIN organizer");
        }

        organizerRepository.delete(organizer);
    }

    @Override
    public void transferOwnership(Long eventId, Long newVendorId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = getCurrentUser();

        EventOrganizer currentMain;

        if(user.getRole().equals(Role.ADMIN)){
            currentMain = organizerRepository
                    .findByEventIdAndRole(eventId, OrganizerRole.MAIN)
                    .orElseThrow(() -> new NotFoundException("Main organizer not found"));
        }else{
            Vendor currentVendor = getCurrentVendor();
            currentMain = organizerRepository
                    .findByEventIdAndVendorId(eventId, currentVendor.getId())
                    .orElseThrow(() -> new NotFoundException("Main organizer not found"));
            if(currentMain.getRole() != OrganizerRole.MAIN){
                throw new UnauthorizedException("You are not owner of this event");
            }
        }

        EventOrganizer newOwner = organizerRepository
                .findByEventIdAndVendorId(eventId, newVendorId)
                .orElseThrow(() -> new BadRequestException("New owner must already be an organizer"));

        if (newOwner.getRole() == OrganizerRole.MAIN) {
            throw new ConflictException("Already MAIN organizer");
        }

        currentMain.setRole(OrganizerRole.CO_ORGANIZER);
        newOwner.setRole(OrganizerRole.MAIN);

        organizerRepository.saveAll(List.of(currentMain,newOwner));
    }

    // helper methods
    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private Vendor getCurrentVendor() {
        User user = getCurrentUser();

        return vendorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("User is not a vendor"));
    }

    private void checkOwnership(Long eventId, Long vendorId) {
        boolean isOwner = organizerRepository
                .existsByEventIdAndVendorIdAndRole(eventId, vendorId, OrganizerRole.MAIN);

        if (!isOwner) {
            throw new UnauthorizedException("You are not owner of this event");
        }
    }

    private List<OrganizerResponse> map(Long eventId) {
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
}
