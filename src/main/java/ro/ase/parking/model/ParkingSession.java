package ro.ase.parking.model;

import java.time.LocalDateTime;

public class ParkingSession {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    public ParkingSession(Long id, LocalDateTime start) {
        this.id = id;
        this.start = start;
    }

    public ParkingSession(LocalDateTime start) {
        this.start = start;
    }

    public void endSession(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Long getId() {
        return id;
    }
}

