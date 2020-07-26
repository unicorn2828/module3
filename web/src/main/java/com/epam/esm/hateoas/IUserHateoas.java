package com.epam.esm.hateoas;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UsersDto;

/**
 * This is the IUserHateoas interface; it extends {@link IBaseHateoas} interface.
 * <p>
 * Please see the {@link IBaseHateoas} class for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface IUserHateoas extends IBaseHateoas<UserDto, UsersDto> {
}
