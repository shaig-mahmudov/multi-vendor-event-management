package org.ironhack.project.eventmanagement.service.review.impl;

import org.ironhack.project.eventmanagement.dto.request.review.CreateReviewRequest;
import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;
import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.Review;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.mapper.ReviewMapper;
import org.ironhack.project.eventmanagement.repository.BookingItemRepository;
import org.ironhack.project.eventmanagement.repository.EventRepository;
import org.ironhack.project.eventmanagement.repository.ReviewRepository;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.service.review.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final BookingItemRepository bookingItemRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            EventRepository eventRepository,
            BookingItemRepository bookingItemRepository,
            UserRepository userRepository,
            ReviewMapper reviewMapper
    ) {
        this.reviewRepository = reviewRepository;
        this.eventRepository = eventRepository;
        this.bookingItemRepository = bookingItemRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    @Override
    public ReviewResponse createReview(Long eventId, CreateReviewRequest request) {
        User user = requireCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!bookingItemRepository.userHasBookingForEvent(user.getId(), eventId)) {
            throw new BadRequestException("You can only review events you have booked");
        }
        if (reviewRepository.existsByEventIdAndUserId(eventId, user.getId())) {
            throw new BadRequestException("You have already reviewed this event");
        }

        Review review = new Review();
        review.setEvent(event);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    @Override
    public List<ReviewResponse> getReviewsForEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }

        return reviewRepository.findByEventIdOrderByCreatedAtDesc(eventId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public Double getAverageRatingForEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        return reviewRepository.getAverageRatingForEvent(eventId);
    }

    private User requireCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
