package org.ironhack.project.eventmanagement.dto.request.organizer;

import jakarta.validation.constraints.NotNull;

public class TransferOwnershipRequest {

    @NotNull(message = "Vendor id is required")
    private Long newVendorId;

    public Long getNewVendorId() {
        return newVendorId;
    }

    public void setNewVendorId(Long newVendorId) {
        this.newVendorId = newVendorId;
    }
}
