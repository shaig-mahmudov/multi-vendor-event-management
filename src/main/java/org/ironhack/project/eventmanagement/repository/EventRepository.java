package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByStatus(EventStatus status);
    List<Event> findByCategoryIdAndStatus(Long categoryId, EventStatus status);
    List<Event> findByStatusAndDateAfter(EventStatus status, LocalDateTime date);
    List<Event> findByStatusAndDateBefore(EventStatus status, LocalDateTime date);
    List<Event> findByStatusAndDateBetween(EventStatus status, LocalDateTime start, LocalDateTime end);
    List<Event> findByCategoryIdAndStatusAndDateAfter(Long categoryId, EventStatus status, LocalDateTime date);
    List<Event> findByCategoryIdAndStatusAndDateBefore(Long categoryId, EventStatus status, LocalDateTime date);
    List<Event> findByCategoryIdAndStatusAndDateBetween(Long categoryId, EventStatus status, LocalDateTime start, LocalDateTime end);
}
