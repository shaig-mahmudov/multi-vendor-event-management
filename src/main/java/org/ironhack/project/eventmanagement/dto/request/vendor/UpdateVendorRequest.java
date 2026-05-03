package org.ironhack.project.eventmanagement.dto.request.vendor;

public class UpdateVendorRequest {
    @jakarta.validation.constraints.Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    @jakarta.validation.constraints.Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @jakarta.validation.constraints.Size(max = 500, message = "Logo URL must be at most 500 characters")
    private String logoUrl;

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
