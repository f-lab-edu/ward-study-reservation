package com.dsg.wardstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_TARGET_1PARAM,
    NO_TARGET_2PARAM,
    DUPLICATED_ID,

    INTERNAL_SERVER_ERROR,
    INVALID_REQUEST,

}
