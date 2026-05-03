package org.ironhack.project.eventmanagement.auth;

import org.ironhack.project.eventmanagement.auth.request.*;
import org.ironhack.project.eventmanagement.auth.response.AuthResponse;
import org.ironhack.project.eventmanagement.auth.response.UserInfoResponse;
import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.repository.*;
import org.ironhack.project.eventmanagement.security.jwt.JwtService;
import org.ironhack.project.eventmanagement.service.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final VendorRepository vendorRepository;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository,
            JwtBlacklistRepository jwtBlacklistRepository,
            VerificationTokenRepository verificationTokenRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService, VendorRepository vendorRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.vendorRepository = vendorRepository;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(normalizeRole(request.getRole()));
        user.setEmailVerified(false);
        user.setFailedLoginAttempts(0);

        var saved = userRepository.save(user);

        if (saved.getRole() == Role.VENDOR) {
            var vendor = new Vendor();
            vendor.setUser(saved);
            vendor.setName(saved.getName());
            vendorRepository.save(vendor);
        }

        var verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(saved);
        verificationToken.setExpiryDate(Instant.now().plusSeconds(86400)); // 24 hours
        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(saved.getEmail(), verificationToken.getToken());

        var principal = org.springframework.security.core.userdetails.User.builder()
                .username(saved.getEmail())
                .password(saved.getPassword())
                .authorities("ROLE_" + saved.getRole().name())
                .build();

        var token = jwtService.generateToken(principal);
        var refreshToken = createRefreshToken(saved);

        return new AuthResponse(token, refreshToken.getToken(), jwtService.getExpirationSeconds(), toUserInfo(saved));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        var userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("Invalid credentials");
        }
        var user = userOpt.get();

        if (!user.isEmailVerified()) {
            throw new UnauthorizedException("Email not verified. Please check your email to verify your account.");
        }

        if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new UnauthorizedException("Account is locked. Please try again later.");
        }

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            var principal = (UserDetails) authentication.getPrincipal();

            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
            userRepository.save(user);

            var token = jwtService.generateToken(principal);
            var refreshToken = createRefreshToken(user);

            return new AuthResponse(token, refreshToken.getToken(), jwtService.getExpirationSeconds(), toUserInfo(user));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(15));
            }
            userRepository.save(user);
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    @Override
    public UserInfoResponse me(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toUserInfo(user);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token expired");
        }

        var user = refreshToken.getUser();
        var principal = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

        var token = jwtService.generateToken(principal);
        return new AuthResponse(token, refreshToken.getToken(), jwtService.getExpirationSeconds(), toUserInfo(user));
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        var token = request.getRefreshToken();
        if (token != null && !token.isBlank()) {
            try {
                var expiration = Instant.now().plusSeconds(jwtService.getExpirationSeconds());
                var blacklist = new JwtBlacklist();
                blacklist.setToken(token);
                blacklist.setExpiryDate(expiration);
                jwtBlacklistRepository.save(blacklist);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid or expired verification token"));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Verification token expired");
        }

        var user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        var userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return;
        }

        var user = userOpt.get();

        passwordResetTokenRepository.deleteByUser(user);

        var resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(Instant.now().plusSeconds(3600));
        passwordResetTokenRepository.save(resetToken);

        String resetTokenValue = resetToken.getToken();
        emailService.sendPasswordResetEmail(user.getEmail(), resetTokenValue);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        var resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired password reset token"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Password reset token expired");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        var user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setAccountLockedUntil(null);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
        refreshTokenRepository.deleteByUser(user);
    }

    private RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        var refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(86400 * 30));
        return refreshTokenRepository.save(refreshToken);
    }

    private static Role normalizeRole(Role requested) {
        if (requested == null) return Role.CUSTOMER;
        if (requested == Role.ADMIN) return Role.CUSTOMER;
        return requested;
    }

    private static UserInfoResponse toUserInfo(User user) {
        return new UserInfoResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
