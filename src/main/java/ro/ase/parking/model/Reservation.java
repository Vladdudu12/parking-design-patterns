package ro.ase.parking.model;

import java.time.LocalDateTime;

public class Reservation {
    private Long id;
    private User user;
    private LocalDateTime start;
    private LocalDateTime end;

    public Reservation(Long id, User user, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public Reservation(User user, LocalDateTime start, LocalDateTime end) {
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStart() {
        return start;
    }
}

