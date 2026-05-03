package org.ironhack.project.eventmanagement.service.user;

import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInteractionService {
    void favoriteEvent(Long eventId);
    void unfavoriteEvent(Long eventId);
    Page<EventResponse> getMyFavoriteEvents(Pageable pageable);

    void followVendor(Long vendorId);
    void unfollowVendor(Long vendorId);
    Page<VendorResponse> getMyFollowedVendors(Pageable pageable);
}
