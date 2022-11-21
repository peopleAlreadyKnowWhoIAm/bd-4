package ua.iot.labs.jdbcProjects.datasource;

import java.util.List;
import java.util.Optional;

public interface BasicDao<T> {
    List<T> findAll();

    Optional<T> findById(Integer id);
    List<T> findByName(String name);

    Integer create(T obj);

    int update(T obj);

    int delete(Integer id);
}
