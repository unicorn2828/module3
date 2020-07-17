package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITagRepository extends IBaseRepository<Tag> {

    Optional<Tag> findByName(String name);

    void delete(long id);
}
