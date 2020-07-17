package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateValidator {
    private static final long MAX_DURATION = 365;
    private static final long MIN_DURATION = 1;
    private static final long MAX_PRICE = 100;
    private static final long MIN_PRICE = 0;
    private static final long MAX_DESCRIPTION = 100;
    private static final long MIN_DESCRIPTION = 3;
    private static final long MAX_NAME = 30;
    private static final long MIN_NAME = 3;
    private ServiceExceptionCode errorCode;
    private final TagValidator tagValidator;

    public boolean isCertificate(CertificateDto certificate) {
        errorCode = NO_ERROR;
        if (certificate == null) {
            errorCode = CERTIFICATE_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        isName(certificate.getCertificateName().strip());
        isDescription(certificate.getDescription());
        isPrice(certificate.getPrice());
        isDuration(certificate.getDuration());
        if (certificate.getTags() != null) {
            certificate.getTags().stream().allMatch(tagValidator::isTag);
        }
        return true;
    }

    public boolean isId(Long id) {
        errorCode = NO_ERROR;
        if (id == null) {
            errorCode = CERTIFICATE_ID_IS_NULL;
        } else if (id < 1) {
            errorCode = CERTIFICATE_ID_LESS_THAN_1;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            System.out.println(errorCode);
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    public boolean isName(String name) {
        errorCode = NO_ERROR;
        if (name == null) {
            errorCode = CERTIFICATE_NAME_IS_NULL;
        } else if (name.isEmpty()) {
            errorCode = CERTIFICATE_NAME_IS_EMPTY;
        } else if (name.length() > MAX_NAME) {
            errorCode = CERTIFICATE_NAME_MORE_30;
        } else if (name.length() < MIN_NAME) {
            errorCode = CERTIFICATE_NAME_LESS_THAN_3;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    private boolean isDescription(String description) {
        errorCode = NO_ERROR;
        if (description == null) {
            errorCode = CERTIFICATE_DESCRIPTION_IS_NULL;
        } else if (description.length() > MAX_DESCRIPTION) {
            errorCode = CERTIFICATE_DESCRIPTION_MORE_100;
        } else if (description.length() > 0 && description.length() < MIN_DESCRIPTION) {
            errorCode = CERTIFICATE_DESCRIPTION_LESS_THAN_3;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    public boolean isPrice(BigDecimal price) {
        errorCode = NO_ERROR;
        if (price == null) {
            errorCode = CERTIFICATE_PRICE_IS_NULL;
        } else if (price.compareTo(new BigDecimal(MAX_PRICE)) > 0) {
            errorCode = CERTIFICATE_PRICE_MORE_THAN_100;
        } else if (price.compareTo(new BigDecimal(MIN_PRICE)) < 0) {
            errorCode = CERTIFICATE_PRICE_LESS_THAN_0;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    public boolean isDate(LocalDate creationDate) {
        errorCode = NO_ERROR;
        if (creationDate == null) {
            errorCode = DATE_CREATION_IS_NULL;
        } else if (creationDate.isAfter(LocalDate.now())) {
            errorCode = DATE_CREATION_AFTER_TODAY;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }

    private boolean isDuration(Integer duration) {
        errorCode = NO_ERROR;
        if (duration == null) {
            errorCode = CERTIFICATE_DURATION_IS_NULL;
        } else if (duration > MAX_DURATION) {
            errorCode = CERTIFICATE_DURATION_MORE_THAN_365;
        } else if (duration < MIN_DURATION) {
            errorCode = CERTIFICATE_DURATION_LESS_THAN_1;
        }
        if (errorCode == NO_ERROR) {
            return true;
        } else {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
    }


}
