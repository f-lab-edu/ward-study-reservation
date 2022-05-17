package com.dsg.wardstudy.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
    private String detailMessage;

    public ResourceNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ResourceNotFoundException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }


}
