package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UsersDto;
import com.epam.esm.hateoas.IUserHateoas;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas implements IUserHateoas {

    @Override
    public UserDto add(UserDto user) {
        user.add(linkTo(methodOn(UserController.class).find(user.getId())).withSelfRel());
        return user;
    }

    @Override
    public UsersDto add(UsersDto users) {
        users.getUsers().forEach(user -> add(user));
        users.add(linkTo(methodOn(UserController.class).findAll(new HashMap<>())).withSelfRel());
        return users;
    }
}
