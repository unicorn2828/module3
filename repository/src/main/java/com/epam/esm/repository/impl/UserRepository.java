package com.epam.esm.repository.impl;

import com.epam.esm.model.User;
import com.epam.esm.repository.IUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {
    private static final String LOGIN = "login";
    private static final String FIND_BY_NAME = "SELECT u FROM User u WHERE u.login = :login";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> find(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        TypedQuery<User> query = entityManager.createQuery(FIND_BY_NAME, User.class);
        return query.setParameter(LOGIN, login).getResultStream().findFirst();
    }

    @Override
    public List<User> findAll(int pageNumber, int pageSize, String sql){
        TypedQuery<User> query = entityManager.createQuery(sql, User.class);
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();}

    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return entityManager.find(User.class, user.getId());
    }
}
