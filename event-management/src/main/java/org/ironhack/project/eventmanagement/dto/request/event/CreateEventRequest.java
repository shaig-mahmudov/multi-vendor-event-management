package org.ironhack.project.eventmanagement.dto.request.event;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class CreateEventRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50")
    private String title;
    @NotBlank
    @Size(max = 255, message = "Description can be max 255 characters")
    private String description;
    @NotBlank(message = "Date is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime date;
    @NotBlank(message = "Location is required")
    private String location;
    private String imageUrl;
    @NotBlank(message = "Category is required")
    private Long categoryId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}