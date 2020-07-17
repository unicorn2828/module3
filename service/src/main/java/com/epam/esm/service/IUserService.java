package com.epam.esm.service;

import com.epam.esm.dto.AuthUserDto;
import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UsersDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService, IBaseService<UserDto, UsersDto> {

    UserDto register(UserDto userDto);

    ResponseEntity<?> signIn(AuthUserDto authUserDto);

    OrdersDto findUserOrders(long id);
}
