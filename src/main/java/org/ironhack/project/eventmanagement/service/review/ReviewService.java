package org.ironhack.project.eventmanagement.service.review;

import org.ironhack.project.eventmanagement.dto.request.review.CreateReviewRequest;
import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewResponse createReview(Long eventId, CreateReviewRequest request);

    Page<ReviewResponse> getReviewsForEvent(Long eventId, Pageable pageable);

    Double getAverageRatingForEvent(Long eventId);

    ReviewResponse updateReview(Long reviewId, org.ironhack.project.eventmanagement.dto.request.review.UpdateReviewRequest request);

    void deleteReview(Long reviewId);
}
