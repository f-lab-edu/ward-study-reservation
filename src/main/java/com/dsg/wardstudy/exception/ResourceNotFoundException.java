package com.dsg.wardstudy.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private ErrorCode errorCode;
    private String errorEntity;
    private Long errorArgumentLongId;
    private String errorArgumentStringId;

    public ResourceNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ResourceNotFoundException(ErrorCode errorCode, Long errorArgumentLongId, String errorEntity) {
        this.errorCode = errorCode;
        this.errorEntity = errorEntity;
        this.errorArgumentLongId = errorArgumentLongId;
    }    
    
    public ResourceNotFoundException(ErrorCode errorCode, String errorArgumentStringId, String errorEntity) {
        this.errorCode = errorCode;
        this.errorEntity = errorEntity;
        this.errorArgumentStringId = errorArgumentStringId;
    }

    public ResourceNotFoundException(ErrorCode errorCode, Long errorArgumentLongId, String errorArgumentStringId, String errorEntity) {
        this.errorCode = errorCode;
        this.errorEntity = errorEntity;
        this.errorArgumentLongId = errorArgumentLongId;
        this.errorArgumentStringId = errorArgumentStringId;
    }

    public String errorMessage() {
        switch (errorCode) {
            case NO_TARGET_1PARAM:
                return String.format("errorArgumentId: %s, not found entity: %s", errorArgumentLongId, errorEntity);
            case NO_TARGET_2PARAM:
                return String.format("argumentLongId: %d, argumentStringId: %s, not found entity: %s", errorArgumentLongId, errorArgumentStringId, errorEntity);
            case DUPLICATED_ID:
                return String.format("errorArgumentId: %d, Id is duplicated for entity: %s", errorArgumentLongId, errorEntity);
            case INVALID_REQUEST:
                return String.format("errorArgumentId: %d, it is invalid argument for entity: %s", errorArgumentLongId, errorEntity);
            case INTERNAL_SERVER_ERROR:
                return "internal_server_error";
        }
        return "";
    }



}
