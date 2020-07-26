package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificatePriceDto;
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
    private ServiceExceptionCode errorCode;
    private final TagValidator tagValidator;
    private final CommonValidator commonValidator;

    public boolean isCertificate(CertificateDto certificate) {
        errorCode = NO_ERROR;
        if (certificate == null) {
            errorCode = CERTIFICATE_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        if (certificate.getCertificateName() == null) {
            errorCode = CERTIFICATE_NAME_IS_NULL;
        }
        if (certificate.getDescription() == null) {
            errorCode = CERTIFICATE_DESCRIPTION_IS_NULL;
        }
        if (certificate.getPrice() == null) {
            errorCode = CERTIFICATE_PRICE_IS_NULL;
        }
        if (certificate.getDuration() == null) {
            errorCode = CERTIFICATE_DURATION_IS_NULL;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        isName(certificate.getCertificateName().trim());
        isDescription(certificate.getDescription());
        isDuration(certificate.getDuration());
        isPrice(certificate.getPrice());
        if (certificate.getTags() != null) {
            certificate.getTags()
                       .stream()
                       .allMatch(tagValidator::isTag);
        }
        return true;
    }

    public boolean isId(Long certificateId) {
        return commonValidator.isId(certificateId);
    }

    public boolean isName(String certificateName) {
        return commonValidator.isName(certificateName);
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
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }

    public boolean isPrice(BigDecimal price) {
        errorCode = NO_ERROR;
        if (price == null) {
            errorCode = CERTIFICATE_PRICE_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        if (price.compareTo(new BigDecimal(MAX_PRICE)) > 0) {
            errorCode = CERTIFICATE_PRICE_MORE_THAN_100;
        } else if (price.compareTo(new BigDecimal(MIN_PRICE)) < 0) {
            errorCode = CERTIFICATE_PRICE_LESS_THAN_0;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }

    public boolean isNewPrice(CertificatePriceDto newPrice) {
        errorCode = NO_ERROR;
        if (newPrice == null) {
            errorCode = CERTIFICATE_PRICE_IS_NULL;
        } else if (newPrice.getNewPrice() == null) {
            errorCode = NEW_PRICE_PARAMETER_NOT_FOUND;
        } else if (!commonValidator.isNumber(newPrice.getNewPrice().trim())) {
            errorCode = CERTIFICATE_PRICE_IS_NOT_NUMBER;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        isPrice(new BigDecimal(newPrice.getNewPrice().trim()));
        return true;
    }

    public boolean isDate(LocalDate creationDate) {
        errorCode = NO_ERROR;
        if (creationDate == null) {
            errorCode = DATE_CREATION_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else if (creationDate.isAfter(LocalDate.now())) {
            errorCode = DATE_CREATION_AFTER_TODAY;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }

    private boolean isDuration(Integer duration) {
        errorCode = NO_ERROR;
        if (duration == null) {
            errorCode = CERTIFICATE_DURATION_IS_NULL;
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        } else if (duration > MAX_DURATION) {
            errorCode = CERTIFICATE_DURATION_MORE_THAN_365;
        } else if (duration < MIN_DURATION) {
            errorCode = CERTIFICATE_DURATION_LESS_THAN_1;
        }
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }
}
