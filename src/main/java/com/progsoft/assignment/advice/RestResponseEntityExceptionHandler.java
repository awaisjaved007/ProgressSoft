package com.progsoft.assignment.advice;

import com.progsoft.assignment.exceptions.MissingDataException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;


@ControllerAdvice(basePackages = "com.progsoft.assignment")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {MissingDataException.class})
    public ResponseEntity<Object> handleMissingData(final RuntimeException ex,
                                                    final WebRequest request) {
        return ErrorMessage.create(HttpStatus.BAD_REQUEST, ex.getMessage(),
                request.getContextPath());

    }


    @lombok.Data
    private static class ErrorMessage {
        private Timestamp timestamp;
        private HttpStatus status;
        private String error;
        private List<?> errors;
        private String message;
        private String path;

        private static ResponseEntity<Object> create(final HttpStatus status, final String error,
                                                     final String path) {
            final ErrorMessage message = new ErrorMessage();

            message.timestamp = new Timestamp(System.currentTimeMillis());
            message.status = status;
            message.error = status.toString();
            message.errors = Collections.EMPTY_LIST;
            message.message = error;
            message.path = path;

            return new ResponseEntity<>(message, new HttpHeaders(), status);
        }
    }
}