package org.ironhack.project.eventmanagement.service.review;

import org.ironhack.project.eventmanagement.dto.request.review.CreateReviewRequest;
import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(Long eventId, CreateReviewRequest request);

    List<ReviewResponse> getReviewsForEvent(Long eventId);

    Double getAverageRatingForEvent(Long eventId);
}
