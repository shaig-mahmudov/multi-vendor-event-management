package org.ironhack.project.eventmanagement.dto.response;

public class OrganizerResponse {
    private Long id;
    private Long vendorId;
    private String vendorName;
    private String role;

    public OrganizerResponse(Long id, Long vendorId, String vendorName, String role) {
        this.id = id;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getRole() {
        return role;
    }
}
