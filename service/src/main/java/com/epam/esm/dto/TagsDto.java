package com.epam.esm.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class TagsDto extends RepresentationModel<TagsDto> {
    List<TagDto> tags;
}
