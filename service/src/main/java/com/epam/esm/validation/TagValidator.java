package com.epam.esm.validation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Component
public class TagValidator {
    private static final long MAX_NAME = 30;
    private static final long MIN_NAME = 3;
    private ServiceExceptionCode errorCode;

    public boolean isTag(TagDto tag) {
        if (tag == null) {
            errorCode = TAG_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else {
            isName(tag.getName());
            return true;
        }
    }

    public boolean isId(Long id) {
        errorCode = NO_ERROR;
        if (id == null) {
            errorCode = TAG_ID_IS_NULL;
        } else if (id < 1) {
            errorCode = TAG_ID_LESS_THAN_1;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    public boolean isName(String name) {
        errorCode = NO_ERROR;
        if (name == null) {
            errorCode = TAG_NAME_IS_NULL;
        } else if (name.isEmpty()) {
            errorCode = TAG_NAME_IS_EMPTY;
        } else if (name.length() > MAX_NAME) {
            errorCode = TAG_NAME_MORE_THAN_30;
        } else if (name.length() < MIN_NAME) {
            errorCode = TAG_NAME_LESS_THAN_3;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }
}
