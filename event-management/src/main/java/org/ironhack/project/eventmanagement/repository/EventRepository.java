package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.*;
import org.ironhack.project.eventmanagement.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByStatus(EventStatus status);
    List<Event> findByCategoryIdAndStatus(Long categoryId, EventStatus status);
}
