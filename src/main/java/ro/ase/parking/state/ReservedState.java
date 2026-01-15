package ro.ase.parking.state;

import ro.ase.parking.model.ParkingSession;
import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;

import java.time.LocalDateTime;

public class ReservedState implements ParkingState {

    public void reserve(ParkingSpot spot, Reservation reservation) {
        throw new IllegalStateException("Already reserved");
    }

    public void occupy(ParkingSpot spot) {
        spot.setSession(new ParkingSession(LocalDateTime.now()));
        spot.setState(new OccupiedState());
    }

    public Double release(ParkingSpot spot) {
        spot.setReservation(null);
        spot.setState(new AvailableState());
        return null;
    }

    public String getStatus() {
        return "REZERVAT";
    }
}

