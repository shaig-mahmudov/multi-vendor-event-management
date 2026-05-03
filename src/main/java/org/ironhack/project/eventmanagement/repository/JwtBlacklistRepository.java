package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findByToken(String token);
    boolean existsByToken(String token);
    void deleteByExpiryDateBefore(Instant now);
}
