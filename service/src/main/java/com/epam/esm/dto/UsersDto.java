package com.epam.esm.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class UsersDto extends RepresentationModel<UsersDto> {
    List<UserDto> users;
}
