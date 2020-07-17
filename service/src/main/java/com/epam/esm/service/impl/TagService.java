package com.epam.esm.service.impl;


import com.epam.esm.builder.impl.TagQueryBuilder;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.mapper.DtoMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.ITagRepository;
import com.epam.esm.service.ITagService;
import com.epam.esm.validation.TagValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Service("tagService")
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final DataProcessingService service;
    private final TagQueryBuilder queryBuilder;
    private final ITagRepository tagRepository;
    private final TagValidator validator;
    private final DtoMapper mapper;

    @Override
    public TagDto find(long id) {
        validator.isId(id);
        Tag tag = tagRepository.find(id).orElseThrow(() -> new ServiceException(TAG_WITH_THIS_ID_DOES_NOT_EXIST));
        return mapper.toTagDto(tag);
    }

    @Override
    public TagsDto findAll(Map<String, String> allParams) {
        Map<String, String> params = service.toLowerCase(allParams);
        int pageNumber = service.receivePageNumber(params);
        int pageSize = service.receivePageSize(params);
        List<Tag> tagList = tagRepository.findAll(pageNumber, pageSize, queryBuilder.buildQuery(params));
        List<TagDto> tagDtoList = tagList.stream()
                                         .map(mapper::toTagDto)
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
    public void delete(long id) {
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
    public TagDto create(TagDto dto) {
        if (tagRepository.findByName(dto.getName()).isPresent()) {
            ServiceExceptionCode errorCode = TAG_WITH_THIS_NAME_ALREADY_EXISTS;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        Tag tag = tagRepository.save(mapper.toTag(dto));
        log.info("tag {} created", tag.getId());
        return mapper.toTagDto(tag);
    }
}
