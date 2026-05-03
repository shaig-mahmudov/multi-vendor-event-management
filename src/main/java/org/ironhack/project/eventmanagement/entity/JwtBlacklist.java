package org.ironhack.project.eventmanagement.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "jwt_blacklist",
        indexes = {
                @Index(name = "idx_token", columnList = "token"),
                @Index(name = "idx_expiry", columnList = "expiryDate")
        }
)
public class JwtBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
