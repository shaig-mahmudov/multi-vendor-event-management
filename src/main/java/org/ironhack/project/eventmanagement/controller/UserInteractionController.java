package org.ironhack.project.eventmanagement.controller;

import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.service.user.UserInteractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class UserInteractionController {

    private final UserInteractionService userInteractionService;

    public UserInteractionController(UserInteractionService userInteractionService) {
        this.userInteractionService = userInteractionService;
    }

    @PostMapping("/favorites/{eventId}")
    public void favoriteEvent(@PathVariable Long eventId) {
        userInteractionService.favoriteEvent(eventId);
    }

    @DeleteMapping("/favorites/{eventId}")
    public void unfavoriteEvent(@PathVariable Long eventId) {
        userInteractionService.unfavoriteEvent(eventId);
    }

    @GetMapping("/favorites")
    public Page<EventResponse> getMyFavoriteEvents(Pageable pageable) {
        return userInteractionService.getMyFavoriteEvents(pageable);
    }

    @PostMapping("/following/{vendorId}")
    public void followVendor(@PathVariable Long vendorId) {
        userInteractionService.followVendor(vendorId);
    }

    @DeleteMapping("/following/{vendorId}")
    public void unfollowVendor(@PathVariable Long vendorId) {
        userInteractionService.unfollowVendor(vendorId);
    }

    @GetMapping("/following")
    public Page<VendorResponse> getMyFollowedVendors(Pageable pageable) {
        return userInteractionService.getMyFollowedVendors(pageable);
    }
}
