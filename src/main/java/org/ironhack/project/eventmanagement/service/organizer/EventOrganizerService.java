package org.ironhack.project.eventmanagement.service.organizer;

import org.ironhack.project.eventmanagement.dto.request.organizer.AddOrganizerRequest;
import org.ironhack.project.eventmanagement.dto.response.OrganizerResponse;

import java.util.List;

public interface EventOrganizerService {
    OrganizerResponse addOrganizer(Long eventId, AddOrganizerRequest request);

    List<OrganizerResponse> getOrganizers(Long eventId);

    void removeOrganizer(Long organizerId);
    void transferOwnership(Long eventId, Long newVendorId);
}
