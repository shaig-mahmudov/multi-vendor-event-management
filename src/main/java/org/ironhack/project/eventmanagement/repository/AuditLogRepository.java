package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
