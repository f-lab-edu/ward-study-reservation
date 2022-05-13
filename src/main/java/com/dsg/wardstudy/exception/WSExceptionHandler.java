package com.dsg.wardstudy.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.dsg.wardstudy.exception.ErrorCode.INTERNAL_SERVER_ERROR;


@Slf4j
@ControllerAdvice
public class WSExceptionHandler extends ResponseEntityExceptionHandler {

    // handle sepecific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest) {

        log.error("ResourceNotFoundException: ", exception);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(LocalDateTime.now())
                .message(exception.errorMessage())
                .description(webRequest.getDescription(false))
                .errorCode(exception.getErrorCode())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // handle sepecific exceptions
    @ExceptionHandler(WSApiException.class)
    public ResponseEntity<ErrorDetails> handleWSApiException(
            WSApiException exception,
            WebRequest webRequest) {

        log.error("WSApiException: ", exception);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(LocalDateTime.now())
                .message(exception.getMessage())
                .description(webRequest.getDescription(false))
                .errorCode(exception.getErrorCode())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception exception,
            WebRequest webRequest) {

        log.error("Exception ", exception);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(LocalDateTime.now())
                .message(exception.getMessage())
                .description(webRequest.getDescription(false))
                .errorCode(INTERNAL_SERVER_ERROR)
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //BindingResult Validation 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("MethodArgumentNotValidException: ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
