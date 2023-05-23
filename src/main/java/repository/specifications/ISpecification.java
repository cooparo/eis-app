package repository.specifications;

public interface ISpecification<T> {

    boolean exist(T t);

}
