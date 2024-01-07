package com.solution.xpresss_assessment.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class XpressException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public XpressException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
