package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;
import com.epam.esm.hateoas.ITagHateoas;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.epam.esm.model.Role.ROLE_ADMIN;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoas implements ITagHateoas {
    private static final String DELETE_OPERATION = "delete";

    @Override
    public TagDto add(TagDto tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        tag.add(linkTo(methodOn(TagController.class).findTagById(tag.getId())).withSelfRel());
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(ROLE_ADMIN.getAuthority()))) {
            tag.add(linkTo(methodOn(TagController.class).removeTag(tag.getId())).withRel(DELETE_OPERATION));
        }
        return tag;
    }

    @Override
    public TagsDto add(TagsDto tags) {
        tags.getTags().forEach(tag -> add(tag));
        tags.add(linkTo(methodOn(TagController.class).findAll(new HashMap<>())).withSelfRel());
        return tags;
    }
}
