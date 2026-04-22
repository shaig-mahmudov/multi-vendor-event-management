package org.ironhack.project.eventmanagement.auth;

import org.ironhack.project.eventmanagement.auth.request.LoginRequest;
import org.ironhack.project.eventmanagement.auth.request.RegisterRequest;
import org.ironhack.project.eventmanagement.auth.response.AuthResponse;
import org.ironhack.project.eventmanagement.auth.response.UserInfoResponse;
import org.ironhack.project.eventmanagement.entity.Role;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.exception.BadRequestException;
import org.ironhack.project.eventmanagement.exception.NotFoundException;
import org.ironhack.project.eventmanagement.exception.UnauthorizedException;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.ironhack.project.eventmanagement.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(normalizeRole(request.getRole()));

        var saved = userRepository.save(user);

        var principal = org.springframework.security.core.userdetails.User.builder()
                .username(saved.getEmail())
                .password(saved.getPassword())
                .authorities("ROLE_" + saved.getRole().name())
                .build();

        var token = jwtService.generateToken(principal);
        return new AuthResponse(token, jwtService.getExpirationSeconds(), toUserInfo(saved));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            var principal = (UserDetails) authentication.getPrincipal();

            var user = userRepository.findByEmail(principal.getUsername())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            var token = jwtService.generateToken(principal);
            return new AuthResponse(token, jwtService.getExpirationSeconds(), toUserInfo(user));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    @Override
    public UserInfoResponse me(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toUserInfo(user);
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
