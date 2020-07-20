package com.epam.esm.exception;

import lombok.Data;

@Data
public class ApiError {
    private String errorMessage;
    private String errorCode;

    public ApiError(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
