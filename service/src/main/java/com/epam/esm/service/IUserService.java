package com.epam.esm.service;

import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.TagsDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UsersDto;

/**
 * The IUserService interface; it extends {@link IBaseService} class.
 * <p>
 * Please see the {@link IBaseService} class for true identity.
 *
 * @author Vitaly Kononov
 */
public interface IUserService extends IBaseService<UserDto, UsersDto> {

    /**
     * Finds user orders by user id.
     *
     * @param userId - id of user
     * @return the user orders.
     */
    OrdersDto findUserOrders(final Long userId);

    /**
     * Finds the most widely used secondary entity of a user with the highest cost of all orders.
     *
     * @param userId - id of user
     * @return the most widely used tag.
     */
    TagsDto findUserSuperTag(final Long userId);
}
