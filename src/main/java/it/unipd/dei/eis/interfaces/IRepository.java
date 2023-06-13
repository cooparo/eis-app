package it.unipd.dei.eis.interfaces;

import java.util.ArrayList;

public interface IRepository<T> {
    void add(T... t);
    ArrayList<T> getAll();
    void update(T oldT, T newT);
    void remove(String id);
    void removeAll();
    int size();
    ArrayList<T> FindBySpecification(ISpecification spec);
}
