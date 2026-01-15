package ro.ase.parking.state;

import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;

public class AvailableState implements ParkingState {

    public void reserve(ParkingSpot spot, Reservation reservation) {
        spot.setReservation(reservation);
        spot.setState(new ReservedState());
    }

    public void occupy(ParkingSpot spot) {
        throw new IllegalStateException("Cannot occupy without reservation");
    }

    public Double release(ParkingSpot spot) {
        throw new IllegalStateException("Already free");
    }

    public String getStatus() {
        return "LIBER";
    }
}

