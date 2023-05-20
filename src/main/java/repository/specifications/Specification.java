package repository.specifications;

public interface Specification<T> {

    boolean exist(T t);

}
