package ro.ase.parking.state;

import ro.ase.parking.model.ParkingSession;
import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;

import java.time.LocalDateTime;

public class OccupiedState implements ParkingState {

    public void reserve(ParkingSpot spot, Reservation reservation) {
        throw new IllegalStateException("Occupied");
    }

    public void occupy(ParkingSpot spot) {
        throw new IllegalStateException("Already occupied");
    }

    public Double release(ParkingSpot spot) {
        ParkingSession session = spot.getSession();
        double hours = 1;
        double cost = hours * spot.getPricePerHour();
        session.endSession(LocalDateTime.now());

        spot.setSession(null);
        spot.setReservation(null);
        spot.setState(new AvailableState());

        return cost;
    }

    public String getStatus() {
        return "OCUPAT";
    }
}

