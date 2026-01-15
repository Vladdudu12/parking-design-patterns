package ro.ase.parking.model;

import ro.ase.parking.state.AvailableState;
import ro.ase.parking.state.ParkingState;

public class ParkingSpot {
    private Long id;
    private double pricePerHour;
    private ParkingState state;
    private Reservation reservation;
    private ParkingSession session;
    private User currentUser;

    public ParkingSpot(Long id, double pricePerHour) {
        this.id = id;
        this.pricePerHour = pricePerHour;
        this.state = new AvailableState();
    }

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public ParkingSession getSession() {
        return session;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public String getStatus() {
        return state.getStatus();
    }

    public void reserve(Reservation reservation) {
        this.reservation = reservation;
        this.currentUser = reservation.getUser();
        state.reserve(this, reservation);
    }

    public void occupy() {
        state.occupy(this);
    }

    public Double release() {
        return state.release(this);
    }

    public void setState(ParkingState state) {
        this.state = state;
    }

    public void setReservation(Reservation r) {
        this.reservation = r;
    }

    public void setSession(ParkingSession s) {
        this.session = s;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
