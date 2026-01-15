package ro.ase.parking.specification;

import ro.ase.parking.model.Reservation;

import java.time.LocalDateTime;

public class TimeIntervalSpecification implements Specification<Reservation> {

    @Override
    public boolean isSatisfiedBy(Reservation r) {
        return r.getEnd().isAfter(LocalDateTime.now());
    }
}
