package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEventIdOrderByCreatedAtDesc(Long eventId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    @Query("select avg(r.rating) from Review r where r.event.id = :eventId")
    Double getAverageRatingForEvent(@Param("eventId") Long eventId);
}
