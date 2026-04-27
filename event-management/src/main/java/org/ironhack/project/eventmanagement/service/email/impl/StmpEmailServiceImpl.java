package org.ironhack.project.eventmanagement.service.email.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.ironhack.project.eventmanagement.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Real SMTP email service for production profile.
 * Activate with: spring.profiles.active=prod
 */
@Service
public class SmtpEmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.base-url}")
    private String baseUrl;

    public SmtpEmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + token;

        String subject = "Verify your email address";
        String body = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 24px; color: #333;">
                  <h2>Welcome! Please verify your email</h2>
                  <p>Click the button below to activate your account:</p>
                  <a href="%s"
                     style="display:inline-block; padding:12px 24px; background:#4F46E5;
                            color:#fff; border-radius:6px; text-decoration:none; font-weight:bold;">
                    Verify Email
                  </a>
                  <p style="margin-top:16px; font-size:12px; color:#888;">
                    Or copy this link: %s
                  </p>
                  <p style="font-size:12px; color:#888;">
                    This link expires in 24 hours. If you didn't register, ignore this email.
                  </p>
                </body>
                </html>
                """.formatted(verificationUrl, verificationUrl);

        sendHtmlEmail(to, subject, body);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = baseUrl + "/api/auth/reset-password?token=" + token;

        String subject = "Reset your password";
        String body = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 24px; color: #333;">
                  <h2>Password Reset Request</h2>
                  <p>Click the button below to reset your password:</p>
                  <a href="%s"
                     style="display:inline-block; padding:12px 24px; background:#DC2626;
                            color:#fff; border-radius:6px; text-decoration:none; font-weight:bold;">
                    Reset Password
                  </a>
                  <p style="margin-top:16px; font-size:12px; color:#888;">
                    Or copy this link: %s
                  </p>
                  <p style="font-size:12px; color:#888;">
                    This link expires in 1 hour. If you didn't request this, ignore this email.
                  </p>
                </body>
                </html>
                """.formatted(resetUrl, resetUrl);

        sendHtmlEmail(to, subject, body);
    }

    // ── private helper ──────────────────────────────────────────────────────────

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(message);
            log.info("Email sent → {} | subject: {}", to, subject);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }
}