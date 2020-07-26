package com.epam.esm.exception;

import com.epam.esm.exception.data.ExceptionCode;
import com.epam.esm.exception.data.ExceptionMessage;

/**
 * This is the ServiceExceptionCode enum class.
 * This class contains all exceptions and links to the codes and messages of those exceptions.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public enum ServiceExceptionCode {
    NO_ERROR(ExceptionCode.ERROR_000000, ExceptionMessage.ERROR_000000),

    TAG_IS_NULL(ExceptionCode.ERROR_000001, ExceptionMessage.ERROR_000001),
    ID_NOT_NUMBER(ExceptionCode.ERROR_000002, ExceptionMessage.ERROR_000002),
    NAME_IS_NULL(ExceptionCode.ERROR_000003, ExceptionMessage.ERROR_000003),
    NAME_IS_EMPTY(ExceptionCode.ERROR_000004, ExceptionMessage.ERROR_000004),
    NAME_LESS_THAN_3(ExceptionCode.ERROR_000005, ExceptionMessage.ERROR_000005),
    NAME_MORE_THAN_30(ExceptionCode.ERROR_000006, ExceptionMessage.ERROR_000006),
    TAG_WITH_THIS_NAME_DOES_NOT_EXIST(ExceptionCode.ERROR_000008, ExceptionMessage.ERROR_000008),
    TAG_ID_IS_NULL(ExceptionCode.ERROR_000009, ExceptionMessage.ERROR_000009),
    TAG_WITH_THIS_NAME_ALREADY_EXISTS(ExceptionCode.ERROR_000110, ExceptionMessage.ERROR_000110),
    TAG_WITH_THIS_ID_DOES_NOT_EXIST(ExceptionCode.ERROR_000101, ExceptionMessage.ERROR_000101),

    CERTIFICATE_IS_NULL(ExceptionCode.ERROR_000010, ExceptionMessage.ERROR_000010),
    ID_IS_NULL(ExceptionCode.ERROR_000020, ExceptionMessage.ERROR_000020),
    ID_LESS_THAN_1(ExceptionCode.ERROR_000021, ExceptionMessage.ERROR_000021),
    CERTIFICATE_NAME_IS_NULL(ExceptionCode.ERROR_000030, ExceptionMessage.ERROR_000030),
    CERTIFICATE_NAME_IS_EMPTY(ExceptionCode.ERROR_000031, ExceptionMessage.ERROR_000031),
    CERTIFICATE_NAME_MORE_30(ExceptionCode.ERROR_000032, ExceptionMessage.ERROR_000032),
    CERTIFICATE_NAME_LESS_THAN_3(ExceptionCode.ERROR_000033, ExceptionMessage.ERROR_000033),
    CERTIFICATE_WITH_THIS_NAME_DOES_NOT_EXIST(ExceptionCode.ERROR_000034, ExceptionMessage.ERROR_000034),
    CERTIFICATE_WITH_THIS_ID_DOES_NOT_EXIST(ExceptionCode.ERROR_000035, ExceptionMessage.ERROR_000035),
    CERTIFICATE_BY_THIS_PARAM_DOES_NOT_EXIST(ExceptionCode.ERROR_000036, ExceptionMessage.ERROR_000036),
    CERTIFICATE_IS_ORDERED(ExceptionCode.ERROR_000037, ExceptionMessage.ERROR_000037),

    CERTIFICATE_DESCRIPTION_IS_NULL(ExceptionCode.ERROR_000040, ExceptionMessage.ERROR_000040),
    CERTIFICATE_DESCRIPTION_LESS_THAN_3(ExceptionCode.ERROR_000041, ExceptionMessage.ERROR_000041),
    CERTIFICATE_DESCRIPTION_MORE_100(ExceptionCode.ERROR_000042, ExceptionMessage.ERROR_000042),
    CERTIFICATE_PRICE_LESS_THAN_0(ExceptionCode.ERROR_000050, ExceptionMessage.ERROR_000050),
    CERTIFICATE_PRICE_MORE_THAN_100(ExceptionCode.ERROR_000051, ExceptionMessage.ERROR_000051),
    CERTIFICATE_PRICE_IS_NULL(ExceptionCode.ERROR_000052, ExceptionMessage.ERROR_000052),
    CERTIFICATE_PRICE_IS_NOT_NUMBER(ExceptionCode.ERROR_000053, ExceptionMessage.ERROR_000053),
    NEW_PRICE_PARAMETER_NOT_FOUND(ExceptionCode.ERROR_000054, ExceptionMessage.ERROR_000054),
    DATE_CREATION_IS_NULL(ExceptionCode.ERROR_000060, ExceptionMessage.ERROR_000060),
    DATE_CREATION_AFTER_TODAY(ExceptionCode.ERROR_000061, ExceptionMessage.ERROR_000061),
    DATE_MODIFICATION_IS_NULL(ExceptionCode.ERROR_000062, ExceptionMessage.ERROR_000062),
    DATE_MODIFICATION_EARLY_THAN_TODAY(ExceptionCode.ERROR_000063, ExceptionMessage.ERROR_000063),
    CERTIFICATE_DURATION_LESS_THAN_1(ExceptionCode.ERROR_000070, ExceptionMessage.ERROR_000070),
    CERTIFICATE_DURATION_MORE_THAN_365(ExceptionCode.ERROR_000071, ExceptionMessage.ERROR_000071),
    CERTIFICATE_DURATION_IS_NULL(ExceptionCode.ERROR_000072, ExceptionMessage.ERROR_000072),
    PARAMETER_IS_NULL(ExceptionCode.ERROR_000073, ExceptionMessage.ERROR_000073),

    UNKNOWN_EXCEPTION(ExceptionCode.ERROR_000080, ExceptionMessage.ERROR_000080),
    CONSTRAINT_VIOLATION_EXCEPTION(ExceptionCode.ERROR_000081, ExceptionMessage.ERROR_000081),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION(ExceptionCode.ERROR_000082, ExceptionMessage.ERROR_000082),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(ExceptionCode.ERROR_000083, ExceptionMessage.ERROR_000083),
    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(ExceptionCode.ERROR_000084, ExceptionMessage.ERROR_000084),
    NO_HANDLE_FOUND_EXCEPTION(ExceptionCode.ERROR_000085, ExceptionMessage.ERROR_000085),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(ExceptionCode.ERROR_000086, ExceptionMessage.ERROR_000086),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION(ExceptionCode.ERROR_000087, ExceptionMessage.ERROR_000087),

    ORDER_IS_NULL(ExceptionCode.ERROR_000090, ExceptionMessage.ERROR_000090),
    ORDER_IS_EMPTY(ExceptionCode.ERROR_000091, ExceptionMessage.ERROR_000091),
    CERTIFICATES_PARAMETER_NOT_FOUND(ExceptionCode.ERROR_000092, ExceptionMessage.ERROR_000092),

    UNKNOWN_PARAMETER(ExceptionCode.ERROR_000100, ExceptionMessage.ERROR_000100),
    PAGE_NUMBER_OR_SIZE_LESS_1(ExceptionCode.ERROR_000102, ExceptionMessage.ERROR_000102),
    PAGE_NUMBER_OR_SIZE_NOT_INTEGER(ExceptionCode.ERROR_000103, ExceptionMessage.ERROR_000103),
    NUMBER_PAGE_SIZE_NOT_INTEGER(ExceptionCode.ERROR_000104, ExceptionMessage.ERROR_000104),
    ORDER_WITH_THIS_ID_DOES_NOT_EXIST(ExceptionCode.ERROR_000120, ExceptionMessage.ERROR_000120),

    EXPIRED_OR_INVALID_JWT_TOKEN(ExceptionCode.ERROR_000140, ExceptionMessage.ERROR_000140),

    PASSWORD_AND_CONFIRM_ARE_NOT_EQUAL(ExceptionCode.ERROR_000150, ExceptionMessage.ERROR_000150),
    USER_WITH_THIS_LOGIN_DOES_NOT_EXIST(ExceptionCode.ERROR_000151, ExceptionMessage.ERROR_000151),
    USER_WITH_THE_SAME_LOGIN_ALREADY_EXISTS(ExceptionCode.ERROR_000152, ExceptionMessage.ERROR_000152),
    INVALID_LOGIN_OR_PASSWORD(ExceptionCode.ERROR_000153, ExceptionMessage.ERROR_000153),
    USER_WITH_THIS_ID_DOES_NOT_EXIST(ExceptionCode.ERROR_000154, ExceptionMessage.ERROR_000154),
    ACCESS_FORBIDDEN(ExceptionCode.ERROR_000170, ExceptionMessage.ERROR_000170);

    private String exceptionCode;
    private String exceptionMessage;

    ServiceExceptionCode(String exceptionId, String exceptionMessage) {
        this.exceptionCode = exceptionId;
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
