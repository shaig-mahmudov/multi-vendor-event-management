package org.ironhack.project.eventmanagement.service.user;

import org.ironhack.project.eventmanagement.dto.request.user.ChangePasswordRequest;
import org.ironhack.project.eventmanagement.dto.request.user.UpdateUserRequest;
import org.ironhack.project.eventmanagement.dto.response.UserResponse;

public interface UserService {
    UserResponse getMe();

    UserResponse updateMe(UpdateUserRequest request);

    void changeMyPassword(ChangePasswordRequest request);
}
