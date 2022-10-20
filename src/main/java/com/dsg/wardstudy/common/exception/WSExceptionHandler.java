package com.dsg.wardstudy.common.exception;

import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dsg.wardstudy.common.exception.ErrorHttpStatusMapper.mapToStatus;


@Log4j2
@ControllerAdvice
public class WSExceptionHandler extends ResponseEntityExceptionHandler {

    // handle specific exceptions
    @ExceptionHandler(WSApiException.class)
    public ResponseEntity<Object> handleWSApiException(
            WSApiException exception,
            WebRequest request) {


        log.error("WSApiException: ", exception);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(TimeParsingUtils.formatterString(LocalDateTime.now()))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .errorCode(exception.getErrorCode())
                .build();

        log.info("errorDetails: {}", errorDetails);
        return new ResponseEntity<>(errorDetails, mapToStatus(errorDetails.getErrorCode()));
    }

    //BindingResult Validation 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("MethodArgumentNotValidException: ", ex);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(TimeParsingUtils.formatterString(LocalDateTime.now()))
                .message(Optional.ofNullable(ex.getBindingResult()
                                .getFieldError())
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .orElse(ex.getMessage()))
                .description(request.getDescription(false))
                .errorCode(ErrorCode.INVALID_REQUEST)
                .build();

        log.error("errorDetails: {}", errorDetails);
        return new ResponseEntity<>(errorDetails, mapToStatus(errorDetails.getErrorCode()));

    }

}
