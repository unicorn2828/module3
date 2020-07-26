package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.hateoas.IUserHateoas;
import com.epam.esm.hateoas.impl.OrderHateoas;
import com.epam.esm.hateoas.impl.TagHateoas;
import com.epam.esm.service.IRegistrationService;
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

/**
 * This is the UserController class.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final IRegistrationService registrationService;
    private final OrderHateoas orderHateoas;
    private final IUserService userService;
    private final TagHateoas tagHateoas;
    private final IUserHateoas hateoas;

    @PostMapping(value = "/logIn")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> logIn(@Valid @RequestBody AuthUserDto authUserDto) {
        return registrationService.signIn(authUserDto);
    }

    @PostMapping(value = "/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        System.out.println("1");
        UserDto user = registrationService.register(userDto);
        return hateoas.add(user);
    }

    @GetMapping(value = "/{userId}/orders")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public OrdersDto findUserOrders(@Valid @PathVariable final Long userId) {
        OrdersDto orders = userService.findUserOrders(userId);
        return orderHateoas.add(orders);
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserDto find(@Valid @PathVariable("userId") @Positive final Long userId) {
        UserDto user = userService.find(userId);
        return hateoas.add(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UsersDto findAll(@Valid @RequestParam Map<String, String> allParams) {
        UsersDto users = userService.findAll(allParams);
        return hateoas.add(users);
    }

    @GetMapping(value = "/{userId}/superTag")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public TagsDto findSuperTag(@Valid @PathVariable @Positive final Long userId) {
        TagsDto tags = userService.findUserSuperTag(userId);
        return tagHateoas.add(tags);
    }
}
