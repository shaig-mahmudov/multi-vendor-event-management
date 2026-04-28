package org.ironhack.project.eventmanagement.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private static final int MIN_KEY_BYTES = 32;
    private final SecretKey signingKey;
    private final long expirationSeconds;
    private final String issuer;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds,
            @Value("${app.jwt.issuer:event-management}") String issuer
    ) {
        this.signingKey = Keys.hmacShaKeyFor(decodeAndValidateSecret(secret));
        this.expirationSeconds = expirationSeconds;
        this.issuer = issuer;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public String generateToken(UserDetails userDetails) {
        var now = Instant.now();
        var exp = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        var claims = parseClaims(token);
        var username = claims.getSubject();
        var expired = claims.getExpiration().before(new Date());
        var issuerMatches = issuer.equals(claims.getIssuer());
        return username != null
                && username.equals(userDetails.getUsername())
                && !expired
                && issuerMatches;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static byte[] decodeAndValidateSecret(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("app.jwt.secret must be set");
        }

        var trimmed = secret.trim();
        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(trimmed);
        } catch (IllegalArgumentException ignored) {
            keyBytes = trimmed.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < MIN_KEY_BYTES) {
            throw new IllegalStateException(
                    "app.jwt.secret is too short: must be at least " + MIN_KEY_BYTES +
                            " bytes (" + (MIN_KEY_BYTES * 8) + " bits). Got " + keyBytes.length + " bytes."
            );
        }

        return keyBytes;
    }
}