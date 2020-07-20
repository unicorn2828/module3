package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class TagDto extends BaseDto {

    @NotBlank(message = "Tag  can`t be null/spaces!")
    @Pattern(regexp = "(^[A-Za-zА-Яа-я0-9]+$)", message = "Tag name must consist of letters.")
    @Size(min = 3, max = 30, message = "Tag  must be bigger than 3 symbols and smaller than 30")
    private String tagName;
}
