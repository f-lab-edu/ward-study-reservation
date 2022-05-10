package com.dsg.wardstudy.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ErrorDetails {

    private LocalDateTime date;
    private String message;
    private String description;
    private ErrorCode errorCode;

    @Builder
    public ErrorDetails(LocalDateTime date, String message, String description, ErrorCode errorCode) {
        this.date = date;
        this.message = message;
        this.description = description;
        this.errorCode = errorCode;
    }
}
