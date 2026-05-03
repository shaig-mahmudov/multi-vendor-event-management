package org.ironhack.project.eventmanagement.dto.response;

import org.ironhack.project.eventmanagement.entity.OrganizerRole;

public class OrganizerResponse {
    private Long id;
    private Long vendorId;
    private String vendorName;
    private OrganizerRole role;

    public OrganizerResponse(Long id, Long vendorId, String vendorName, OrganizerRole role) {
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

    public OrganizerRole getRole() {
        return role;
    }
}
