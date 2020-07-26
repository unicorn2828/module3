package com.epam.esm.validation;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.epam.esm.exception.ServiceExceptionCode.PAGE_NUMBER_OR_SIZE_LESS_1;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaginationValidator {
    private static final String NUMBER_PATTER = "-?\\d+(\\.\\d+)?";
    private ServiceExceptionCode errorCode;

    public boolean isPageNumberOrSize(int number) {
        if (number < 1) {
            errorCode = PAGE_NUMBER_OR_SIZE_LESS_1;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage() + " " + number);
            throw new ServiceException(errorCode);
        }
        return true;
    }

    public boolean isNumber(String number) {
        if (number == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(NUMBER_PATTER);
        return pattern.matcher(number).matches();
    }
}
