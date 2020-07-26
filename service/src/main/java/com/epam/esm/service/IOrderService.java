package com.epam.esm.service;

import com.epam.esm.dto.BookingDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;
import org.springframework.security.core.Authentication;

/**
 * The IOrderService interface; it extends {@link IBaseService} class.
 * <p>
 * Please see the {@link IBaseService} class for true identity.
 *
 * @author Vitaly Kononov
 */
public interface IOrderService extends IBaseService<OrderDto, OrdersDto> {

    /**
     * Creates order.
     *
     * @param orderDto - order for saving to db
     * @return the current order after saving.
     */
    OrderDto create(Authentication authentication, BookingDto orderDto);
}
