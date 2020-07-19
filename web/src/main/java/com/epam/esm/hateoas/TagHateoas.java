package com.epam.esm.hateoas;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.epam.esm.model.Role.ROLE_ADMIN;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoas {
    private static final String DELETE_OPERATION = "delete";

    public TagDto add(TagDto tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        tag.add(linkTo(methodOn(TagController.class).find(tag.getId())).withSelfRel());
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(ROLE_ADMIN.getAuthority()))) {
            tag.add(linkTo(methodOn(TagController.class).remove(tag.getId())).withRel(DELETE_OPERATION));
        }
        return tag;
    }

    public TagsDto add(TagsDto tags) {
        tags.getTags().forEach(tag -> add(tag));
        return tags;
    }
}