package com.epam.esm.hateoas;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UsersDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas {

    public UserDto add(UserDto user) {
            user.add(linkTo(methodOn(UserController.class).find(user.getId())).withSelfRel());
        return user;
    }

    public UsersDto add(UsersDto users) {
        users.getUsers().forEach(user -> add(user));
        return users;
    }
}
