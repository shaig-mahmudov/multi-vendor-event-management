package org.ironhack.project.eventmanagement.dto.request.admin;

import jakarta.validation.constraints.NotNull;
import org.ironhack.project.eventmanagement.entity.Role;

public class UpdateUserRoleRequest {
    @NotNull(message = "Role is required")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

