package org.ironhack.project.eventmanagement.dto.request.category;

import jakarta.validation.constraints.*;

public class CategoryRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3,max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @Size(max = 255, message = "Description can't exceed 255 characters")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
