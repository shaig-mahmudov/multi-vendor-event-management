package org.ironhack.project.eventmanagement.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockEmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(MockEmailServiceImpl.class);

    @Override
    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + token;
        log.info("======================================================");
        log.info("MOCK EMAIL SERVICE: Sending Verification Email");
        log.info("To: {}", to);
        log.info("Link: {}", verificationUrl);
        log.info("======================================================");
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        log.info("======================================================");
        log.info("MOCK EMAIL SERVICE: Sending Password Reset Email");
        log.info("To: {}", to);
        log.info("Link: {}", resetUrl);
        log.info("======================================================");
    }
}
