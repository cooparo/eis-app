package repository;

import repository.specifications.ISpecification;

import java.util.ArrayList;

public interface IRepository<T> {
    void add(T t);
    ArrayList<T> getAll();
    void update(T oldT, T newT);
    void remove(long id);
    ArrayList<T> FindBySpecification(ISpecification spec);

}
