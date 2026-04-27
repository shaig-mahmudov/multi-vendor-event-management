package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.organizer.AddOrganizerRequest;
import org.ironhack.project.eventmanagement.dto.response.OrganizerResponse;
import org.ironhack.project.eventmanagement.service.organizer.EventOrganizerService;
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
    public OrganizerResponse add(@PathVariable Long eventId,
                                 @RequestBody @Valid AddOrganizerRequest request){
        return organizerService.addOrganizer(eventId, request);
    }

    @GetMapping
    public List<OrganizerResponse> get(@PathVariable Long eventId){
        return organizerService.getOrganizers(eventId);
    }

    @DeleteMapping("/{organizerId}")
    public void delete(@PathVariable Long organizerId){
        organizerService.removeOrganizer(organizerId);
    }
}
