package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
}
