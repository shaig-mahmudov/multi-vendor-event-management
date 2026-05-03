package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.admin.UpdateUserRoleRequest;
import org.ironhack.project.eventmanagement.dto.response.UserResponse;
import org.ironhack.project.eventmanagement.service.admin.AdminUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse setRole(@PathVariable Long userId, @Valid @RequestBody UpdateUserRoleRequest request) {
        return adminUserService.setUserRole(userId, request.getRole());
    }
}

