package org.ironhack.project.eventmanagement.service.admin.impl;

import org.ironhack.project.eventmanagement.dto.response.UserResponse;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.service.admin.AdminUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserResponse setUserRole(Long userId, org.ironhack.project.eventmanagement.entity.Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setRole(role);
        return toResponse(userRepository.save(user));
    }

    private static UserResponse toResponse(User user) {
        Long vendorId = user.getVendor() != null ? user.getVendor().getId() : null;
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                vendorId
        );
    }
}

