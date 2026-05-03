package org.ironhack.project.eventmanagement.service.vendor;

import org.ironhack.project.eventmanagement.dto.request.vendor.CreateVendorRequest;
import org.ironhack.project.eventmanagement.dto.request.vendor.UpdateVendorRequest;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;

public interface VendorService {
    VendorResponse createMyVendor(CreateVendorRequest request);

    VendorResponse getMyVendor();

    VendorResponse updateMyVendor(UpdateVendorRequest request);

    VendorResponse getById(Long vendorId);
}
