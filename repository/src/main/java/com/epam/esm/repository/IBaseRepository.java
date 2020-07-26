package com.epam.esm.repository;

import com.epam.esm.model.BaseModel;

import java.util.List;
import java.util.Optional;

/**
 * This is the IBaseRepository interface with the basic methods of all repositories.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface IBaseRepository<T extends BaseModel> {

    /**
     * Finds all of the entities {@link BaseModel} from db.
     *
     * @param pageNumber - page number for pagination
     * @param pageSize   - page size for pagination
     * @param sql        - string query to db {@link String}
     * @return List of entities {@link List}.
     */
    List<T> findAll(int pageNumber, int pageSize, String sql);

    /**
     * Finds an entity {@link BaseModel} by id.
     *
     * @param id - entity id
     * @return the current entity {@link BaseModel} wrapped in an {@link Optional}.
     */
    Optional<T> find(long id);

    /**
     * Saves the current entity {@link BaseModel} to the db.
     *
     * @param t - current entity
     * @return the current entity after saving {@link BaseModel}.
     */
    T save(T t);
}