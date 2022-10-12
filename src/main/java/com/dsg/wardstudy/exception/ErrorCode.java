package com.dsg.wardstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NO_FOUND_ENTITY("존재하지 않는 엔티티입니다."),
    DUPLICATED_ENTITY("이미 존재하는 엔티티입니다."),
    INVALID_REQUEST("요청한 값이 올바르지 않습니다."),
    NOT_FOUND_USER("존재하지 않는 사용자입니다."),
    INTERNAL_SERVER_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");  // 장애 상황

    private final String errorMsg;

}
