package org.ironhack.project.eventmanagement.repository;

import org.ironhack.project.eventmanagement.entity.VendorFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorFollowRepository extends JpaRepository<VendorFollow, Long> {
    boolean existsByUserIdAndVendorId(Long userId, Long vendorId);
    Optional<VendorFollow> findByUserIdAndVendorId(Long userId, Long vendorId);
    Page<VendorFollow> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
