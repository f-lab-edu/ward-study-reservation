package com.dsg.wardstudy.exception;

import org.springframework.http.HttpStatus;

public abstract class ErrorHttpStatusMapper {

    public static HttpStatus mapToStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case NO_FOUND_ENTITY:
                return HttpStatus.NOT_FOUND;
            case DUPLICATED_ENTITY:
            case INVALID_REQUEST:
                return HttpStatus.BAD_REQUEST;
            case NOT_FOUND_USER:
                return HttpStatus.UNAUTHORIZED;
                // TODO: 나머지는 모두 INTERNAL_SERVER_ERROR 처리
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
