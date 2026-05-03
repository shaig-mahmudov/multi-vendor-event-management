package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.BookingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
    @Query("""
            select count(bi) > 0
            from BookingItem bi
            join bi.booking b
            join bi.ticketCategory tc
            where b.user.id = :userId
              and tc.event.id = :eventId
            """)
    boolean userHasBookingForEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
