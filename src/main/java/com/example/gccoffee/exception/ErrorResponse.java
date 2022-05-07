package com.example.gccoffee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private String message;

    private ErrorResponse(String error, String message){
        this.error = error;
        this.message = message;
    }
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(
                        errorCode.getHttpStatus().name(),
                        errorCode.getDetail())
                );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatus httpStatus, String message){
        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(
                        httpStatus.name(),
                        message)
                );
    }
}
