package com.epam.esm.service.impl;


import com.epam.esm.builder.QueryBuilder;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.ITagRepository;
import com.epam.esm.service.DataProcessingService;
import com.epam.esm.service.ITagService;
import com.epam.esm.validation.TagValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.*;
import static com.epam.esm.service.DataProcessingService.PageParamType.PAGE_NUMBER;
import static com.epam.esm.service.DataProcessingService.PageParamType.PAGE_SIZE;

@Slf4j
@Service("tagService")
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final DataProcessingService service;
    private final ITagRepository tagRepository;
    private final QueryBuilder queryBuilder;
    private final TagValidator validator;
    private final ModelMapper mapper;

    @Override
    public TagDto find(Long id) {
        validator.isId(id);
        Tag tag = tagRepository.find(id).orElseThrow(() -> new ServiceException(TAG_WITH_THIS_ID_DOES_NOT_EXIST));
        return mapper.map(tag, TagDto.class);
    }

    @Override
    public TagsDto findAll(Map<String, String> params) {
        params = service.toCamelCase(params);
        int pageNumber = service.receivePageParam(params, PAGE_NUMBER);
        int pageSize = service.receivePageParam(params, PAGE_SIZE);
        List<Tag> tagList = tagRepository.findAll(pageNumber,
                                                  pageSize,
                                                  queryBuilder.buildQuery(params, Tag.class.getSimpleName()));
        List<TagDto> tagDtoList = tagList.stream()
                                         .map(t -> mapper.map(t, TagDto.class))
                                         .collect(Collectors.toList());
        if (tagDtoList.isEmpty()) {
            ServiceExceptionCode exception = TAG_WITH_THIS_NAME_DOES_NOT_EXIST;
            log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
            throw new ServiceException(exception);
        }
        TagsDto tagsDto = new TagsDto();
        tagsDto.setTags(tagDtoList);
        return tagsDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validator.isId(id);
        if (tagRepository.find(id).isPresent()) {
            tagRepository.delete(id);
            log.info("tag {} deleted", id);
        } else {
            ServiceExceptionCode errorCode = TAG_WITH_THIS_ID_DOES_NOT_EXIST;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        validator.isTag(tagDto);
        String tagName = tagDto.getTagName().toLowerCase().trim();
        tagDto.setTagName(tagName);
        if (tagRepository.findByName(tagDto.getTagName()).isPresent()) {
            ServiceExceptionCode errorCode = TAG_WITH_THIS_NAME_ALREADY_EXISTS;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        Tag tag = tagRepository.save(mapper.map(tagDto, Tag.class));
        log.info("tag {} created", tag.getId());
        return mapper.map(tag, TagDto.class);
    }
}
