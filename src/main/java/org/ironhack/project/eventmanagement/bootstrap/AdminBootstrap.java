package org.ironhack.project.eventmanagement.bootstrap;

import org.ironhack.project.eventmanagement.entity.Role;
import org.ironhack.project.eventmanagement.entity.User;
import org.ironhack.project.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap-admin:false}")
    private boolean enabled;

    @Value("${app.bootstrap-admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${app.bootstrap-admin.name:Admin}")
    private String adminName;

    @Value("${app.bootstrap-admin.password:}")
    private String adminPassword;

    public AdminBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!enabled) return;

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("app.bootstrap-admin.password must be set when app.bootstrap-admin=true");
        }

        userRepository.findByEmail(adminEmail).ifPresentOrElse(existing -> {
            if (existing.getRole() != Role.ADMIN) {
                existing.setRole(Role.ADMIN);
                userRepository.save(existing);
            }
        }, () -> {
            var user = new User();
            user.setEmail(adminEmail);
            user.setName(adminName);
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        });
    }
}

