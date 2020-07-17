package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;
import com.epam.esm.hateoas.TagHateoas;
import com.epam.esm.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final ITagService tagService;
    private final TagHateoas hateoas;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public TagDto find(@Valid @PathVariable("id") @Positive final Long id) {
        TagDto tag = tagService.find(id);
        return hateoas.add(tag);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public TagsDto findAll(@Valid @RequestParam Map<String, String> allParams) {
        TagsDto tags = tagService.findAll(allParams);
        return hateoas.add(tags);
    }

    @PostMapping(value = "/")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@Valid @RequestBody(required = false) final TagDto tagDto) {
        TagDto tag =  tagService.create(tagDto);
        return hateoas.add(tag);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> remove(@Valid @PathVariable("id") @Positive final Long id) {
        tagService.delete(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }
}
