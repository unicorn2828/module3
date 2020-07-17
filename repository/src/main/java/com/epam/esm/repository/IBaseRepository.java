package com.epam.esm.repository;

import java.util.List;
import java.util.Optional;

public interface IBaseRepository<T> {

    List<T> findAll(int page, int count, String sql);

    Optional<T> find(long id);

    T save(T t);
}
