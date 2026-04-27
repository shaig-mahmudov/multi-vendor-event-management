package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    List<EventOrganizer> findByVendorId(Long vendorId);

    List<EventOrganizer> findByEventId(Long eventId);
    boolean existsByEventIdAndVendorId(Long eventId, Long vendorId);
}
