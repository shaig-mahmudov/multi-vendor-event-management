package org.ironhack.project.eventmanagement.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateReviewRequest {
    @Min(1)
    @Max(5)
    private Integer rating;
    private String comment;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
