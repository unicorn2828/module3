package com.epam.esm.hateoas;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagsDto;

/**
 * This is the ITagHateoas interface; it extends {@link IBaseHateoas} interface.
 * <p>
 * Please see the {@link IBaseHateoas} class for true identity
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public interface ITagHateoas extends IBaseHateoas<TagDto, TagsDto> {
}
