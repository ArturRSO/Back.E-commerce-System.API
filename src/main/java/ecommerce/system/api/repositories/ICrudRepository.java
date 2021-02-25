package ecommerce.system.api.repositories;

import java.util.List;

public interface ICrudRepository<T> {

    int create(T object);
    List<T> getAll();
    T getById(int id);
    boolean update(T object);
    boolean delete(int id);
}