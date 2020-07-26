package com.epam.esm.validation;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.epam.esm.exception.ServiceExceptionCode.CERTIFICATE_IS_NULL;
import static com.epam.esm.exception.ServiceExceptionCode.NO_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {
    private ServiceExceptionCode errorCode;
    private final CommonValidator commonValidator;

    public boolean isId(Long userId) {
        return commonValidator.isId(userId);
    }

    public boolean isLogin(String login) {
        return commonValidator.isName(login);
    }

    public boolean isUser(UserDto user) {
        errorCode = NO_ERROR;
        if (user == null) {
            errorCode = CERTIFICATE_IS_NULL;
        }
        isLogin(user.getLogin().trim());
        if (errorCode != NO_ERROR) {
            log.error(errorCode.getExceptionCode() + ":" + errorCode.getExceptionMessage());
            throw new ServiceException(errorCode);
        }
        return true;
    }
}
