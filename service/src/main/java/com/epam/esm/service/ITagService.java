package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;

/**
 * The ITagService interface; it extends {@link IBaseService} class.
 * <p>
 * Please see the {@link IBaseService} class for true identity.
 *
 * @author Vitaly Kononov
 */
public interface ITagService extends IBaseService<TagDto, TagsDto> {

    /**
     * Creates tag.
     *
     * @param tagDto - tag for saving to db
     * @return the current tag after saving.
     */
    TagDto create(final TagDto tagDto);

    /**
     * Removes tag by id.
     *
     * @param tagId - id of tag fo removing
     */
    void delete(final Long tagId);
}
