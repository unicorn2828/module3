package com.epam.esm.validation;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.epam.esm.exception.ServiceExceptionCode.PAGE_NUMBER_OR_SIZE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaginationValidator {
    private ServiceExceptionCode errorCode;

    public boolean isPageNumberOrSize(int number) {
        if (number <= 0) {
            errorCode = PAGE_NUMBER_OR_SIZE;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage() + " " + number);
            throw new ServiceException(errorCode);
        }
        return true;
    }

    public boolean isNumber(String number) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (number == null) {
            return false;
        }
        return pattern.matcher(number).matches();
    }
}
