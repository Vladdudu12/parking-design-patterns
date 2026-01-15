package ro.ase.parking.specification;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);
}
