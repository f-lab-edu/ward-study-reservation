package com.dsg.wardstudy.common.exception;

import lombok.Getter;

@Getter
public class WSApiException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Long fieldValue;
    private String fieldValueStr;
    private final ErrorCode errorCode;

    public WSApiException(ErrorCode errorCode) {
        super(errorCode.getErrorMsg());
        this.errorCode = errorCode;
    }

    public WSApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // 그외
    public WSApiException(ErrorCode errorCode, String resourceName, String fieldName, Long fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue)); // 'StudyGroup' not found with 'id' : '1'
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.errorCode = errorCode;
    }

    public WSApiException(ErrorCode errorCode, String resourceName, Long fieldValue) {
        super(String.format("%s is duplicated by '%s'", resourceName, fieldValue)); // 'StudyGroup' is duplicated by '1'
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
        this.errorCode = errorCode;
    }
    // Reservation
    public WSApiException(ErrorCode errorCode, String resourceName, String fieldName, String fieldValueStr) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValueStr)); // 'Reservation' not found with 'id' : '1'
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValueStr = fieldValueStr;
        this.errorCode = errorCode;
    }

    public WSApiException(ErrorCode errorCode, String resourceName, String fieldValueStr) {
        super(String.format("%s is duplicated by '%s'", resourceName, fieldValueStr)); // 'Reservation' is duplicated by '1'
        this.resourceName = resourceName;
        this.fieldValueStr = fieldValueStr;
        this.errorCode = errorCode;
    }

}
