package com.example.erm_demo.domain.exception;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.domain.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .data(e.getMessage())
                .build();
        return ResponseEntity.status(500).body(apiResponse);
    }
    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e){
        ErrorCode  errorCode = e.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }

    @ExceptionHandler(IOException.class)
    ResponseEntity<ApiResponse> handleIOException(IOException e){
        ErrorCode  errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiResponse apiResponse = ApiResponse.builder()
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }


}
