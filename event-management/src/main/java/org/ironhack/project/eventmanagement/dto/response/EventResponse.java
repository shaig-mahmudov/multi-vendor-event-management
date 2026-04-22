package org.ironhack.project.eventmanagement.dto.response;

import org.ironhack.project.eventmanagement.entity.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private String imageUrl;
    private EventStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    private String categoryName;
    private List<OrganizerDTO>  organizers;

    public EventResponse(Long id, String title, String description, LocalDate date, String location, String imageUrl, EventStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<OrganizerDTO> getOrganizers() {
        return organizers;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public EventStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}