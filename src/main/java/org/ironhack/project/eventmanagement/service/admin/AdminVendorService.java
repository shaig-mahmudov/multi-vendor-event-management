package org.ironhack.project.eventmanagement.service.admin;

import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.VendorStatus;

public interface AdminVendorService {
    VendorResponse updateVendorStatus(Long vendorId, VendorStatus status);
}
