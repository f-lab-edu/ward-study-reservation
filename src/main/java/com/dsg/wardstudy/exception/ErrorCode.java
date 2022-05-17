package com.dsg.wardstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_FOUND_ENTITY,
    DUPLICATED_ENTITY,

    INTERNAL_SERVER_ERROR,
    INVALID_REQUEST,

}
