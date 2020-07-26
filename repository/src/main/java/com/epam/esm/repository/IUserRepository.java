package com.epam.esm.repository;

import com.epam.esm.model.User;

import java.util.Optional;

/**
 * This is the IUserRepository interface; it extends IBaseRepository.
 * <p>
 * Please see the {@link IBaseRepository} class for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface IUserRepository extends IBaseRepository<User> {

    /**
     * Finds user by login.
     *
     * @param login - user login for searching in db
     * @return the current user.
     */
    Optional<User> findByLogin(String login);
}
