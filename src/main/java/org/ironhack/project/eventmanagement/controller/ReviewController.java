package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.review.CreateReviewRequest;
import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;
import org.ironhack.project.eventmanagement.service.review.ReviewService;
import org.ironhack.project.eventmanagement.dto.request.review.UpdateReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public Page<ReviewResponse> list(@PathVariable Long eventId, Pageable pageable) {
        return reviewService.getReviewsForEvent(eventId, pageable);
    }

    @GetMapping("/average")
    public Double average(@PathVariable Long eventId) {
        return reviewService.getAverageRatingForEvent(eventId);
    }

    @PatchMapping("/{reviewId}")
    public ReviewResponse update(@PathVariable Long reviewId, @Valid @RequestBody UpdateReviewRequest request) {
        return reviewService.updateReview(reviewId, request);
    }

    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
