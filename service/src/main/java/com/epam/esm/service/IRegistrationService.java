package com.epam.esm.service;

import com.epam.esm.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The IRegistrationService interface; it extends {@link UserDetailsService} class.
 * <p>
 * Please see the {@link UserDetailsService} class for true identity.
 *
 * @author Vitaly Kononov
 */
public interface IRegistrationService extends UserDetailsService {

    /**
     * Registers new user.
     *
     * @param userDto - user for saving to db
     * @return the current new user after saving and registration.
     */
    UserDto register(UserDto userDto);

    /**
     * Authentication a user.
     *
     * @param authUserDto - login and password of user for authentication
     * @return the token of the user after authentication.
     */
    ResponseEntity<?> signIn(AuthUserDto authUserDto);
}
