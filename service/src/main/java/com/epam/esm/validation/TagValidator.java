package com.epam.esm.validation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TagValidator {
    private final CommonValidator commonValidator;
    private ServiceExceptionCode errorCode;

    public boolean isTag(List<TagDto> tags) {
        tags.forEach(this::isTag);
        return true;
    }

    public boolean isTag(TagDto tag) {
        if (tag == null) {
            errorCode = TAG_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else if (tag.getTagName() != null) {
            return isName(tag.getTagName());
        } else {
            errorCode = NAME_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    public boolean isId(Long tagId) {
        return commonValidator.isId(tagId);
    }

    public boolean isName(String tagName) {
        return commonValidator.isName(tagName);
    }
}
