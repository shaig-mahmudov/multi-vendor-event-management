package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.review.CreateReviewRequest;
import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;
import org.ironhack.project.eventmanagement.service.review.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponse create(@PathVariable Long eventId, @Valid @RequestBody CreateReviewRequest request) {
        return reviewService.createReview(eventId, request);
    }

    @GetMapping
    public List<ReviewResponse> list(@PathVariable Long eventId) {
        return reviewService.getReviewsForEvent(eventId);
    }

    @GetMapping("/average")
    public Double average(@PathVariable Long eventId) {
        return reviewService.getAverageRatingForEvent(eventId);
    }
}
