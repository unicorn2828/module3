package com.epam.esm.validation;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Component
public class CommonValidator {
    private static final String NUMBER_PATTER = "-?\\d+(\\.\\d+)?";
    private static final long MAX_NAME_SIZE = 30;
    private static final long MIN_NAME_SIZE = 3;
    private ServiceExceptionCode errorCode;

    public boolean isId(Long id) {
        errorCode = NO_ERROR;
        if (id == null) {
            errorCode = ID_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else if (!(id instanceof Long)) {
            errorCode = ID_NOT_NUMBER;
        } else if (id < 1) {
            errorCode = ID_LESS_THAN_1;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }

    public boolean isName(String name) {
        errorCode = NO_ERROR;
        if (name == null) {
            errorCode = NAME_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else if (name.isEmpty()) {
            errorCode = NAME_IS_EMPTY;
        } else if (name.length() > MAX_NAME_SIZE) {
            errorCode = NAME_MORE_THAN_30;
        } else if (name.length() < MIN_NAME_SIZE) {
            errorCode = NAME_LESS_THAN_3;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }

    public boolean isNumber(String number) {
        if (number == null) {
            errorCode = PARAMETER_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        Pattern pattern = Pattern.compile(NUMBER_PATTER);
        return pattern.matcher(number).matches();
    }
}
