package com.epam.esm.validation;

import com.epam.esm.dto.BookingDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidator {
    private ServiceExceptionCode errorCode;

    public boolean isOrder(BookingDto booking) {
        errorCode = NO_ERROR;
        if (booking == null) {
            errorCode = ORDER_IS_NULL;
        } else if (booking.getCertificates() == null) {
            errorCode = CERTIFICATES_PARAMETER_NOT_FOUND;
        } else if (booking.getCertificates().isEmpty()) {
            errorCode = ORDER_IS_EMPTY;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }
}
