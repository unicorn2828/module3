package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.ITagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

/**
 * This is the TagRepository class; it extends {@link BaseAbstractRepository} class.
 * <p>
 * Please see the {@link BaseAbstractRepository} and {@link ITagRepository} classes for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Repository
public class TagRepository extends BaseAbstractRepository<Tag> implements ITagRepository {
    public static final String TAG_NAME = "tag_name";
    public static final String FIND_BY_NAME_QUERY = "SELECT t FROM Tag t WHERE t.tagName = :tag_name";

    @PersistenceContext
    private EntityManager entityManager;

    public TagRepository(EntityManager em) {
        super(em, Tag.class);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        TypedQuery<Tag> query = entityManager.createQuery(FIND_BY_NAME_QUERY, Tag.class);
        query.setParameter(TAG_NAME, name);
        return query.getResultStream().findFirst();
    }

    @Override
    public void delete(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
        entityManager.flush();
        entityManager.clear();
    }
}
