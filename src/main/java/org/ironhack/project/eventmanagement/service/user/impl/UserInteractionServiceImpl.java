package org.ironhack.project.eventmanagement.service.user.impl;

import org.ironhack.project.eventmanagement.dto.response.EventResponse;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.FavoriteEvent;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.entity.Vendor;
import org.ironhack.project.eventmanagement.entity.VendorFollow;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.mapper.EventMapper;
import org.ironhack.project.eventmanagement.mapper.VendorMapper;
import org.ironhack.project.eventmanagement.repository.EventRepository;
import org.ironhack.project.eventmanagement.repository.FavoriteEventRepository;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.repository.VendorFollowRepository;
import org.ironhack.project.eventmanagement.repository.VendorRepository;
import org.ironhack.project.eventmanagement.service.user.UserInteractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInteractionServiceImpl implements UserInteractionService {

    private final FavoriteEventRepository favoriteEventRepository;
    private final VendorFollowRepository vendorFollowRepository;
    private final EventRepository eventRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final VendorMapper vendorMapper;

    public UserInteractionServiceImpl(FavoriteEventRepository favoriteEventRepository, VendorFollowRepository vendorFollowRepository, EventRepository eventRepository, VendorRepository vendorRepository, UserRepository userRepository, EventMapper eventMapper, VendorMapper vendorMapper) {
        this.favoriteEventRepository = favoriteEventRepository;
        this.vendorFollowRepository = vendorFollowRepository;
        this.eventRepository = eventRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
        this.vendorMapper = vendorMapper;
    }

    @Transactional
    @Override
    public void favoriteEvent(Long eventId) {
        User user = requireCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (favoriteEventRepository.existsByUserIdAndEventId(user.getId(), eventId)) {
            throw new BadRequestException("Event already in favorites");
        }

        FavoriteEvent favorite = new FavoriteEvent();
        favorite.setUser(user);
        favorite.setEvent(event);
        favoriteEventRepository.save(favorite);
    }

    @Transactional
    @Override
    public void unfavoriteEvent(Long eventId) {
        User user = requireCurrentUser();
        FavoriteEvent favorite = favoriteEventRepository.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new NotFoundException("Favorite not found"));

        favoriteEventRepository.delete(favorite);
    }

    @Override
    public Page<EventResponse> getMyFavoriteEvents(Pageable pageable) {
        User user = requireCurrentUser();
        return favoriteEventRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(FavoriteEvent::getEvent)
                .map(eventMapper::toResponse);
    }

    @Transactional
    @Override
    public void followVendor(Long vendorId) {
        User user = requireCurrentUser();
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new NotFoundException("Vendor not found"));

        if (vendorFollowRepository.existsByUserIdAndVendorId(user.getId(), vendorId)) {
            throw new BadRequestException("You are already following this vendor");
        }

        VendorFollow follow = new VendorFollow();
        follow.setUser(user);
        follow.setVendor(vendor);
        vendorFollowRepository.save(follow);
    }

    @Transactional
    @Override
    public void unfollowVendor(Long vendorId) {
        User user = requireCurrentUser();
        VendorFollow follow = vendorFollowRepository.findByUserIdAndVendorId(user.getId(), vendorId)
                .orElseThrow(() -> new NotFoundException("Vendor follow not found"));

        vendorFollowRepository.delete(follow);
    }

    @Override
    public Page<VendorResponse> getMyFollowedVendors(Pageable pageable) {
        User user = requireCurrentUser();
        return vendorFollowRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(VendorFollow::getVendor)
                .map(vendorMapper::toResponse);
    }

    private User requireCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
