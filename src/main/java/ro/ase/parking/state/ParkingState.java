package ro.ase.parking.state;

import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;

public interface ParkingState {
    void reserve(ParkingSpot spot, Reservation reservation);
    void occupy(ParkingSpot spot);
    Double release(ParkingSpot spot);

    String getStatus();
}

