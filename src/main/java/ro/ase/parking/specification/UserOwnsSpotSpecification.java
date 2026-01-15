package ro.ase.parking.specification;

import ro.ase.parking.model.ParkingSpot;
import ro.ase.parking.model.User;

public class UserOwnsSpotSpecification implements Specification<ParkingSpot> {

    private User user;

    public UserOwnsSpotSpecification(User user) {
        this.user = user;
    }

    @Override
    public boolean isSatisfiedBy(ParkingSpot spot) {
        return spot.getReservation() != null && spot.getReservation().getUser().getId().equals(user.getId());
    }
}
