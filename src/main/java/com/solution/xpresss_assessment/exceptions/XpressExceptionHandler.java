package com.solution.xpresss_assessment.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class XpressExceptionHandler {
    @ExceptionHandler(XpressException.class)
    public ResponseEntity<XpressExceptionResponse> handleException(XpressException exception){
        var response = XpressExceptionResponse.builder()
                .message(exception.getMessage())
                .status(exception.getStatus())
                .build();
        return new ResponseEntity<>(response, exception.getStatus());
    }
}
