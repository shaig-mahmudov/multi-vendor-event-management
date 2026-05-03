package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.FavoriteEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    Optional<FavoriteEvent> findByUserIdAndEventId(Long userId, Long eventId);
    Page<FavoriteEvent> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
