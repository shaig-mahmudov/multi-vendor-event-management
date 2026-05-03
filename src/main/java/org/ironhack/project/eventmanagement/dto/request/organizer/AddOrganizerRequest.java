package org.ironhack.project.eventmanagement.dto.request.organizer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.ironhack.project.eventmanagement.entity.OrganizerRole;

public class AddOrganizerRequest {
    @NotNull(message = "Vendor Id is a must")
    @Positive(message = "Vendor Id must be positive")
    private Long vendorId;
    @NotNull(message = "Role can't be empty")
    private OrganizerRole role;

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public OrganizerRole getRole() {
        return role;
    }

    public void setRole(OrganizerRole role) {
        this.role = role;
    }
}