package org.ironhack.project.eventmanagement.dto.request.organizer;

public class AddOrganizerRequest {
    private Long vendorId;
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