package repository;

import repository.specifications.Specification;

import java.util.ArrayList;

public interface Repository<T> {
    void add(T t);
    ArrayList<T> getAll();
    void update(T oldT, T newT);
    void remove(long id);
    ArrayList<T> FindBySpecification(Specification spec);

}
