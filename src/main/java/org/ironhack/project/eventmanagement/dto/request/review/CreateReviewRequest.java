package org.ironhack.project.eventmanagement.dto.request.review;

public class CreateReviewRequest {
    @jakarta.validation.constraints.NotNull(message = "rating is required")
    @jakarta.validation.constraints.Min(value = 1, message = "rating must be between 1 and 5")
    @jakarta.validation.constraints.Max(value = 5, message = "rating must be between 1 and 5")
    private Integer rating;

    @jakarta.validation.constraints.Size(max = 2000, message = "comment must be at most 2000 characters")
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
