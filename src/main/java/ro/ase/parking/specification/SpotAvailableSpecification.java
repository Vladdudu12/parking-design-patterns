package ro.ase.parking.specification;

import ro.ase.parking.model.ParkingSpot;

public class SpotAvailableSpecification implements Specification<ParkingSpot> {

    @Override
    public boolean isSatisfiedBy(ParkingSpot spot) {
        return spot.getStatus().equals("LIBER");
    }
}
