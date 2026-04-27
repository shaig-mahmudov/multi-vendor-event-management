package org.ironhack.project.eventmanagement.auth;

import org.ironhack.project.eventmanagement.auth.request.LoginRequest;
import org.ironhack.project.eventmanagement.auth.request.RegisterRequest;
import org.ironhack.project.eventmanagement.auth.response.AuthResponse;
import org.ironhack.project.eventmanagement.auth.response.UserInfoResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserInfoResponse me(String email);
    
    AuthResponse refreshToken(org.ironhack.project.eventmanagement.auth.request.RefreshTokenRequest request);
    void logout(org.ironhack.project.eventmanagement.auth.request.LogoutRequest request);
    void verifyEmail(String token);
    void forgotPassword(org.ironhack.project.eventmanagement.auth.request.ForgotPasswordRequest request);
    void resetPassword(org.ironhack.project.eventmanagement.auth.request.ResetPasswordRequest request);
}
