package com.epam.esm.exception;

/**
 * This is custom exception class; it extends {@link RuntimeException} class.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
public class ServiceException extends RuntimeException {
    protected String errorCode;
    protected String errorMessage;

    public ServiceException(final String errorCode, final String errorMessage) {
        super(errorCode);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ServiceException(final ServiceExceptionCode errorCode) {
        this(errorCode.getExceptionCode(), errorCode.getExceptionMessage());
        this.errorMessage = errorCode.getExceptionMessage();
        this.errorCode = errorCode.getExceptionCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
