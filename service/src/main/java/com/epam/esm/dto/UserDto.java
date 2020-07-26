package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends BaseDto {

    @NotBlank(message = "login can't be empty")
    @Size(max = 30, min = 3, message = "incorrect length of login - max 30, min 3 symbols")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password can't be empty")
    @Size(max = 50, min = 4, message = "incorrect length of password - max 50, min 4 symbols")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "confirmPassword can't be empty")
    @Size(max = 50, min = 4, message = "incorrect length of confirmPassword - max 50, min 4 symbols")
    private String confirmPassword;

    @NotBlank(message = "email can't be empty")
    @Email(message = "email isn't correct")
    private String email;

    private List<String> role;
    private List<OrderDto> orders;
}
