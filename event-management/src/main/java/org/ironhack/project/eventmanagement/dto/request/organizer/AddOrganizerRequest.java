package org.ironhack.project.eventmanagement.dto.request.organizer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AddOrganizerRequest {
    @NotNull(message = "Vendor Id is a must")
    @Positive(message = "Vendor Id must be positive")
    private Long vendorId;
    @NotBlank(message = "Role can't be empty")
    @Size(max = 50, message = "Role can't be longer than 50 characters")
    private String role;

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}