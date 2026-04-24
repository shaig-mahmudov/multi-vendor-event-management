package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
