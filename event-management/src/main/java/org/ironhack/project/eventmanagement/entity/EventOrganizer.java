package org.ironhack.project.eventmanagement.entity;

import jakarta.persistence.*;

@Entity
public class EventOrganizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String role;
}