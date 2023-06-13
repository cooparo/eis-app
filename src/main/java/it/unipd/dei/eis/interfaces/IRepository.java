package it.unipd.dei.eis.interfaces;

import java.util.ArrayList;

/**
 * An interface that represents a repository, implemented by ArticleRepository.
 */
public interface IRepository<T> {
    void add(T... t);
    ArrayList<T> getAll();
    void update(T oldT, T newT);
    void remove(String id);
    int size();
    ArrayList<T> FindBySpecification(ISpecification spec);
}
