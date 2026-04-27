package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.response.ReviewResponse;
import org.ironhack.project.eventmanagement.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toResponse(Review review) {
        Long userId = review.getUser() != null ? review.getUser().getId() : null;
        String userName = review.getUser() != null ? review.getUser().getName() : null;
        Long eventId = review.getEvent() != null ? review.getEvent().getId() : null;
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                userId,
                userName,
                eventId
        );
    }
}
