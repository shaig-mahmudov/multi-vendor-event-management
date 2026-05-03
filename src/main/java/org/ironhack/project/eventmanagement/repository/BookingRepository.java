package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
