package org.ironhack.project.eventmanagement.service.admin.impl;

import org.ironhack.project.eventmanagement.dto.response.VendorResponse;
import org.ironhack.project.eventmanagement.entity.Role;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.entity.Vendor;
import org.ironhack.project.eventmanagement.entity.VendorStatus;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.mapper.VendorMapper;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.repository.VendorRepository;
import org.ironhack.project.eventmanagement.service.admin.AdminVendorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminVendorServiceImpl implements AdminVendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final VendorMapper vendorMapper;

    public AdminVendorServiceImpl(VendorRepository vendorRepository, UserRepository userRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    @Transactional
    public VendorResponse updateVendorStatus(Long vendorId, VendorStatus status) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new NotFoundException("Vendor not found"));

        vendor.setStatus(status);
        
        User user = vendor.getUser();
        if (status == VendorStatus.APPROVED) {
            if (user.getRole() != Role.ADMIN) {
                user.setRole(Role.VENDOR);
            }
        } else {
            // If suspended or rejected, we might want to revoke VENDOR role
            if (user.getRole() == Role.VENDOR) {
                user.setRole(Role.CUSTOMER);
            }
        }
        
        userRepository.save(user);
        Vendor saved = vendorRepository.save(vendor);
        return vendorMapper.toResponse(saved);
    }
}
