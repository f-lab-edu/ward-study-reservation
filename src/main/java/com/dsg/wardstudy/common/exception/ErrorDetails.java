package com.dsg.wardstudy.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ErrorDetails {

    private String date;
    private String message;
    private String description;
    private ErrorCode errorCode;

    @Builder
    public ErrorDetails(String date, String message, String description, ErrorCode errorCode) {
        this.date = date;
        this.message = message;
        this.description = description;
        this.errorCode = errorCode;
    }
}
