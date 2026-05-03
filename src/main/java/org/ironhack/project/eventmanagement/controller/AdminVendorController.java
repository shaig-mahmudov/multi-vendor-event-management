package org.ironhack.project.eventmanagement.controller;

import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.VendorStatus;
import org.ironhack.project.eventmanagement.service.admin.AdminVendorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/vendors")
@PreAuthorize("hasRole('ADMIN')")
public class AdminVendorController {

    private final AdminVendorService adminVendorService;

    public AdminVendorController(AdminVendorService adminVendorService) {
        this.adminVendorService = adminVendorService;
    }

    @PatchMapping("/{vendorId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public VendorResponse approveVendor(@PathVariable Long vendorId) {
        return adminVendorService.updateVendorStatus(vendorId, VendorStatus.APPROVED);
    }

    @PatchMapping("/{vendorId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public VendorResponse rejectVendor(@PathVariable Long vendorId) {
        return adminVendorService.updateVendorStatus(vendorId, VendorStatus.REJECTED);
    }

    @PatchMapping("/{vendorId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public VendorResponse suspendVendor(@PathVariable Long vendorId) {
        return adminVendorService.updateVendorStatus(vendorId, VendorStatus.SUSPENDED);
    }
}
