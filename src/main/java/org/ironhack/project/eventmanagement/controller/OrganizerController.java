package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.organizer.AddOrganizerRequest;
import org.ironhack.project.eventmanagement.dto.request.organizer.TransferOwnershipRequest;
import org.ironhack.project.eventmanagement.dto.response.OrganizerResponse;
import org.ironhack.project.eventmanagement.service.organizer.EventOrganizerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("events/{eventId}/organizers")
public class OrganizerController {
    private final EventOrganizerService organizerService;
    public OrganizerController(EventOrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public OrganizerResponse add(@PathVariable Long eventId,
                                 @RequestBody @Valid AddOrganizerRequest request){
        return organizerService.addOrganizer(eventId, request);
    }

    @GetMapping
    public List<OrganizerResponse> get(@PathVariable Long eventId){
        return organizerService.getOrganizers(eventId);
    }

    @DeleteMapping("/{organizerId}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public void delete(@PathVariable Long eventId,
                       @PathVariable Long organizerId){
        organizerService.removeOrganizer(organizerId);
    }

    @PatchMapping("/transfer")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public void transferOwnership(@PathVariable Long eventId,
                                  @RequestBody @Valid TransferOwnershipRequest request){
        organizerService.transferOwnership(eventId, request.getNewVendorId());
    }
}
