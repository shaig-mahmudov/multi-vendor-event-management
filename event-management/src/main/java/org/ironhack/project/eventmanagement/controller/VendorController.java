package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.vendor.CreateVendorRequest;
import org.ironhack.project.eventmanagement.dto.request.vendor.UpdateVendorRequest;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.service.vendor.VendorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping("/me")
    public VendorResponse createMyVendor(@Valid @RequestBody CreateVendorRequest request) {
        return vendorService.createMyVendor(request);
    }

    @GetMapping("/me")
    public VendorResponse getMyVendor() {
        return vendorService.getMyVendor();
    }

    @PatchMapping("/me")
    public VendorResponse updateMyVendor(@Valid @RequestBody UpdateVendorRequest request) {
        return vendorService.updateMyVendor(request);
    }

    @GetMapping("/{vendorId}")
    public VendorResponse getById(@PathVariable Long vendorId) {
        return vendorService.getById(vendorId);
    }
}
