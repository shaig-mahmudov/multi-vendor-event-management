package org.ironhack.project.eventmanagement.service.email;

public interface EmailService {
    void sendVerificationEmail(String to, String token);
    void sendPasswordResetEmail(String to, String token);
}
