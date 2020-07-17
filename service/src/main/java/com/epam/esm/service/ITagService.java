package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;

/**
 * The ITagService interface; it extends IBaseService
 *
 * @author Vitaly Kononov
 */
public interface ITagService extends IBaseService<TagDto, TagsDto>{

    TagDto create(final TagDto dto);

    void delete(final long id);
}
