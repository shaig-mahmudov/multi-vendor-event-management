package org.ironhack.project.eventmanagement.service.user.impl;

import org.ironhack.project.eventmanagement.dto.request.user.ChangePasswordRequest;
import org.ironhack.project.eventmanagement.dto.request.user.UpdateUserRequest;
import org.ironhack.project.eventmanagement.dto.response.UserResponse;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.service.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getMe() {
        return toResponse(requireCurrentUser());
    }

    @Transactional
    @Override
    public UserResponse updateMe(UpdateUserRequest request) {
        User user = requireCurrentUser();
        user.setName(request.getName());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public void changeMyPassword(ChangePasswordRequest request) {
        User user = requireCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
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
