package com.epam.esm.controller;

import com.epam.esm.dto.AuthUserDto;
import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UsersDto;
import com.epam.esm.hateoas.OrderHateoas;
import com.epam.esm.hateoas.UserHateoas;
import com.epam.esm.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final OrderHateoas orderHateoas;
    private final IUserService userService;
    private final UserHateoas hateoas;

    @PostMapping(value = "/logIn")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> logIn(@Valid @RequestBody AuthUserDto authUserDto) {
        return userService.signIn(authUserDto);
    }

    @PostMapping(value = "/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.register(userDto);
        return hateoas.add(user);
    }

    @GetMapping(value = "/{userId}/orders")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public OrdersDto findUserOrders(@Valid @PathVariable final long userId) {
        OrdersDto orders = userService.findUserOrders(userId);
        return orderHateoas.add(orders);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto find(@Valid @PathVariable("id") @Positive final Long id) {
        UserDto user = userService.find(id);
        return hateoas.add(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UsersDto findAll(@Valid @RequestParam Map<String, String> allParams) {
        UsersDto users = userService.findAll(allParams);
        return hateoas.add(users);
    }
}
