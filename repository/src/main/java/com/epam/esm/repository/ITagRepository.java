package com.epam.esm.repository;

import com.epam.esm.model.Tag;

import java.util.Optional;

/**
 * This is the ITagRepository interface; it extends IBaseRepository.
 * <p>
 * Please see the {@link IBaseRepository} class for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface ITagRepository extends IBaseRepository<Tag> {

    /**
     * Finds a tag {@link Tag} by name.
     *
     * @param tagName - tag name
     * @return the current tag {@link Tag} wrapped in an {@link Optional}.
     */
    Optional<Tag> findByName(String tagName);

    /**
     * Removes a tag {@link Tag} by id.
     *
     * @param tagId - tag id
     */
    void delete(long tagId);
}
