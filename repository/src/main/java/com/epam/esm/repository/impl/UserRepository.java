package com.epam.esm.repository.impl;

import com.epam.esm.model.User;
import com.epam.esm.repository.IUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

/**
 * This is the UserRepository class; it  extends {@link BaseAbstractRepository} class.
 * <p>
 * Please see the {@link BaseAbstractRepository} and {@link IUserRepository} classes for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Repository
public class UserRepository extends BaseAbstractRepository<User> implements IUserRepository {
    private static final String LOGIN = "login";
    private static final String FIND_BY_NAME = "SELECT u FROM User u WHERE u.login = :login";

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepository(EntityManager em) {
        super(em, User.class);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        TypedQuery<User> query = entityManager.createQuery(FIND_BY_NAME, User.class);
        return query.setParameter(LOGIN, login).getResultStream().findFirst();
    }
}
