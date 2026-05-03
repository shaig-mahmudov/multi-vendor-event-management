package org.ironhack.project.eventmanagement.service.email.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.ironhack.project.eventmanagement.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendVerificationEmail(String to, String token) {
        String link = baseUrl + "/api/auth/verify-email?token=" + token;

        String subject = "Verify your email address";
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head><meta charset="UTF-8"/></head>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 32px;">
                  <div style="max-width: 560px; margin: auto; background: #ffffff;
                              border-radius: 8px; padding: 40px; box-shadow: 0 2px 8px rgba(0,0,0,0.08);">
                    <h2 style="color: #1a1a1a; margin-top: 0;">Confirm your email</h2>
                    <p style="color: #555; line-height: 1.6;">
                      Thanks for signing up! Click the button below to verify your email address.
                      This link expires in <strong>24 hours</strong>.
                    </p>
                    <a href="%s"
                       style="display: inline-block; margin: 24px 0; padding: 12px 28px;
                              background-color: #4f46e5; color: #ffffff; text-decoration: none;
                              border-radius: 6px; font-weight: bold;">
                      Verify Email
                    </a>
                    <p style="color: #999; font-size: 13px;">
                      If you didn't create an account, you can safely ignore this email.<br/>
                      Or copy and paste this link into your browser:<br/>
                      <a href="%s" style="color: #4f46e5; word-break: break-all;">%s</a>
                    </p>
                  </div>
                </body>
                </html>
                """.formatted(link, link, link);

        send(to, subject, body);
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String link = baseUrl + "/api/auth/reset-password?token=" + token;

        String subject = "Reset your password";
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head><meta charset="UTF-8"/></head>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 32px;">
                  <div style="max-width: 560px; margin: auto; background: #ffffff;
                              border-radius: 8px; padding: 40px; box-shadow: 0 2px 8px rgba(0,0,0,0.08);">
                    <h2 style="color: #1a1a1a; margin-top: 0;">Reset your password</h2>
                    <p style="color: #555; line-height: 1.6;">
                      We received a request to reset the password for your account.
                      Click the button below to choose a new one.
                      This link expires in <strong>1 hour</strong>.
                    </p>
                    <a href="%s"
                       style="display: inline-block; margin: 24px 0; padding: 12px 28px;
                              background-color: #dc2626; color: #ffffff; text-decoration: none;
                              border-radius: 6px; font-weight: bold;">
                      Reset Password
                    </a>
                    <p style="color: #999; font-size: 13px;">
                      If you didn't request a password reset, please ignore this email.
                      Your password will not change.<br/>
                      Or copy and paste this link into your browser:<br/>
                      <a href="%s" style="color: #dc2626; word-break: break-all;">%s</a>
                    </p>
                  </div>
                </body>
                </html>
                """.formatted(link, link, link);

        send(to, subject, body);
    }

    private void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = html

            mailSender.send(message);
            log.info("Email '{}' sent to {}", subject, to);
        } catch (MessagingException e) {
            log.error("Failed to send email '{}' to {}: {}", subject, to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}