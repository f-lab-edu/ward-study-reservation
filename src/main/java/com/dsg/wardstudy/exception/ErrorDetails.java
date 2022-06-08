package com.dsg.wardstudy.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class ErrorDetails {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
