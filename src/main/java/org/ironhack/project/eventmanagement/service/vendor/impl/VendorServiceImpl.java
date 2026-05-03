package org.ironhack.project.eventmanagement.service.vendor.impl;

import org.ironhack.project.eventmanagement.dto.request.vendor.CreateVendorRequest;
import org.ironhack.project.eventmanagement.dto.request.vendor.UpdateVendorRequest;
import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.Role;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.entity.Vendor;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.mapper.VendorMapper;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.repository.VendorRepository;
import org.ironhack.project.eventmanagement.service.vendor.VendorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, UserRepository userRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.vendorMapper = vendorMapper;
    }

    @Transactional
    @Override
    public VendorResponse createMyVendor(CreateVendorRequest request) {
        User user = requireCurrentUser();
        if (vendorRepository.existsByUserId(user.getId())) {
            throw new BadRequestException("Vendor already exists for current user");
        }

        Vendor vendor = new Vendor();
        vendor.setName(request.getName());
        vendor.setDescription(request.getDescription());
        vendor.setLogoUrl(request.getLogoUrl());
        vendor.setUser(user);

        vendor.setStatus(org.ironhack.project.eventmanagement.entity.VendorStatus.PENDING);
        user.setVendor(vendor);
        userRepository.save(user);

        Vendor saved = vendorRepository.save(vendor);
        return vendorMapper.toResponse(saved);
    }

    @Override
    public VendorResponse getMyVendor() {
        User user = requireCurrentUser();
        Vendor vendor = vendorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Vendor not found for current user"));
        return vendorMapper.toResponse(vendor);
    }

    @Transactional
    @Override
    public VendorResponse updateMyVendor(UpdateVendorRequest request) {
        User user = requireCurrentUser();
        Vendor vendor = vendorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Vendor not found for current user"));

        if (request.getName() != null) vendor.setName(request.getName());
        if (request.getDescription() != null) vendor.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) vendor.setLogoUrl(request.getLogoUrl());

        return vendorMapper.toResponse(vendorRepository.save(vendor));
    }

    @Override
    public VendorResponse getById(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new NotFoundException("Vendor not found"));
        return vendorMapper.toResponse(vendor);
    }

    private User requireCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
