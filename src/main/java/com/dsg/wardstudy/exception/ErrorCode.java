package com.dsg.wardstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_TARGET("해당되는 대상이 없습니다."),
    DUPLICATED_ID("Id가 중복되어 있습니다."),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;

}
