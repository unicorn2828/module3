package com.epam.esm.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError customHandle(ServiceException ex) {
        return new ApiError(ex.getErrorMessage(), ex.getErrorCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiError handleAll(AccessDeniedException ex) {
        ServiceExceptionCode exception = ServiceExceptionCode.ACCESS_FORBIDDEN;
        return new ApiError(exception.getExceptionMessage(), exception.getExceptionCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleAll(Exception ex) {
        String message = ex.getMessage();
        String errorCode = "no code";
        return  new ApiError(message, errorCode);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleAll(JwtException ex) {
        ServiceExceptionCode exception = ServiceExceptionCode.EXPIRED_OR_INVALID_JWT_TOKEN;
        return new ApiError(exception.getExceptionMessage(), exception.getExceptionCode());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleAll(ValidationException ex) {
        return new ApiError(ex.getMessage().substring(10), ex.getMessage().substring(0, 8));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
        return new ResponseEntity<>(new ApiError(ex.getMessage(), builder.substring(0, builder.length() - 2)),
                                          new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        return new ResponseEntity<>(new ApiError(ex.getMessage(), builder.toString()), new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        String errorMessage = "unknown URL, no method found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        String errorCode = String.valueOf(status.value());
        ApiError apiError = new ApiError(errorMessage, errorCode);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError customHandle(MethodArgumentTypeMismatchException ex) {
        String errorMessage = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        String errorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
        return new ApiError(errorMessage, errorCode);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                       violation.getPropertyPath() + ": " + violation.getMessage());
        }
        String errorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
        return new ApiError(ex.getMessage(), errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return new ResponseEntity<>(new ApiError(ex.getMessage(), error), new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new ApiError(errorMessage, "" + status.value()), new HttpHeaders(), status);
    }
}
