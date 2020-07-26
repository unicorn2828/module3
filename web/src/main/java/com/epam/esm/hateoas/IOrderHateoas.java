package com.epam.esm.hateoas;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;

/**
 * This is the IOrderHateoas interface; it extends {@link IBaseHateoas} interface.
 * <p>
 * Please see the {@link IBaseHateoas} class for true identity.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface IOrderHateoas extends IBaseHateoas<OrderDto, OrdersDto> {
}
