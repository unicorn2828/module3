package com.epam.esm.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public abstract class BaseDto extends RepresentationModel<BaseDto> implements Serializable  {

    @PositiveOrZero
    private Long id;
}
