package com.epam.esm.repository;

import com.epam.esm.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends IBaseRepository<User> {

   Optional<User> findByLogin(String login);
}
