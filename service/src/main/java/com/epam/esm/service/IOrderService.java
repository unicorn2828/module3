package com.epam.esm.service;

import com.epam.esm.dto.BookingDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrdersDto;
import org.springframework.security.core.Authentication;

public interface IOrderService extends IBaseService<OrderDto, OrdersDto>{

    OrderDto create(Authentication authentication, BookingDto bookingDto);
}
