package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthUserDto {

    @NotBlank(message = "login can't be empty")
    @Size(max = 30, min = 3, message = "incorrect length of login - max 30, min 3 symbols")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password can't be empty")
    @Size(max = 50, min = 4, message = "incorrect length of password - max 50, min 4 symbols")
    private String password;
}
