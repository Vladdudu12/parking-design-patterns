package ro.ase.parking.singleton;

import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.Reservation;
import ro.ase.parking.observer.Observer;
import ro.ase.parking.observer.Subject;

import java.util.ArrayList;
import java.util.List;

public class ParkingManager implements Subject {
    private static ParkingManager instance;

    private final List<Observer> observers = new ArrayList<>();

    private ParkingManager() {}

    public static ParkingManager getInstance() {
        if (instance == null) {
            instance = new ParkingManager();
        }
        return instance;
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void notifyObservers(String message) {
        observers.forEach(o -> o.update(message));
    }

    public void reserveSpot(ParkingSpot spot, Reservation reservation) {
        notifyObservers("Locul " + spot.getId() + " a fost rezervat.");
        spot.reserve(reservation);
    }

    public void occupySpot(ParkingSpot spot) {
        notifyObservers("Locul " + spot.getId() + " a fost ocupat.");
        spot.occupy();
    }

    public Double releaseSpot(ParkingSpot spot) {
        notifyObservers("Locul " + spot.getId() + " a fost eliberat.");
        return spot.release();
    }
}
