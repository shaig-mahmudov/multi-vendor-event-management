package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByEventId(Long eventId);
}
