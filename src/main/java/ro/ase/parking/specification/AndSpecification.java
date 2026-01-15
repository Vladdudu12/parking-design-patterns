package ro.ase.parking.specification;

public class AndSpecification<T> implements Specification<T> {
    private Specification<T> a, b;

    public AndSpecification(Specification<T> a, Specification<T> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean isSatisfiedBy(T t) {
        return a.isSatisfiedBy(t) && b.isSatisfiedBy(t);
    }
}

