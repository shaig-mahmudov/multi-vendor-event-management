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

    private OrganizerRole role;

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public OrganizerRole getRole() {
        return role;
    }

    public void setRole(OrganizerRole role) {
        this.role = role;
    }
}