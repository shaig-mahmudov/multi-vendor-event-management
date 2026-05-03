package org.ironhack.project.eventmanagement.controller;

import jakarta.validation.Valid;
import org.ironhack.project.eventmanagement.dto.request.user.ChangePasswordRequest;
import org.ironhack.project.eventmanagement.dto.request.user.UpdateUserRequest;
import org.ironhack.project.eventmanagement.dto.response.UserResponse;
import org.ironhack.project.eventmanagement.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me() {
        return userService.getMe();
    }

    @PatchMapping("/me")
    public UserResponse updateMe(@Valid @RequestBody UpdateUserRequest request) {
        return userService.updateMe(request);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changeMyPassword(request);
        return ResponseEntity.noContent().build();
    }
}
