package org.ironhack.project.eventmanagement.service.admin;

import org.ironhack.project.eventmanagement.dto.response.UserResponse;
import org.ironhack.project.eventmanagement.entity.Role;

public interface AdminUserService {
    UserResponse setUserRole(Long userId, Role role);
}

