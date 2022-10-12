package com.dsg.wardstudy.exception;

import lombok.Getter;

@Getter
public class WSApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public WSApiException(ErrorCode errorCode) {
        super(errorCode.getErrorMsg());
        this.errorCode = errorCode;
    }

    public WSApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
