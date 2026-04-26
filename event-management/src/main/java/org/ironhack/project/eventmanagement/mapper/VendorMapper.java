package org.ironhack.project.eventmanagement.mapper;

import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorMapper {
    public VendorResponse toResponse(Vendor vendor) {
        Long userId = vendor.getUser() != null ? vendor.getUser().getId() : null;
        return new VendorResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getDescription(),
                vendor.getLogoUrl(),
                vendor.getCreatedAt(),
                vendor.getUpdatedAt(),
                userId
        );
    }
}
