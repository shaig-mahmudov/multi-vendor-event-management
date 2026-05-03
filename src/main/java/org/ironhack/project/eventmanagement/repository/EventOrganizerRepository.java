package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.Event;
import org.ironhack.project.eventmanagement.entity.EventOrganizer;
import org.ironhack.project.eventmanagement.entity.OrganizerRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    List<EventOrganizer> findByVendorId(Long vendorId);
    List<EventOrganizer> findByEventId(Long eventId);
    Optional<EventOrganizer> findByEventIdAndRole(Long id, OrganizerRole role);
    Optional<EventOrganizer> findByEventIdAndVendorId(Long eventId, Long vendorId);
    boolean existsByEventIdAndVendorId(Long eventId, Long vendorId);
    boolean existsByEventIdAndVendorIdAndRole(Long eventId, Long vendorId, OrganizerRole role);
}
