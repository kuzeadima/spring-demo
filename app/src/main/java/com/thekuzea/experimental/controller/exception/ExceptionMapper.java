package com.thekuzea.experimental.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thekuzea.experimental.constant.messages.process.AuthenticationMessages;
import com.thekuzea.experimental.domain.dto.error.ErrorResult;

@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleException(final MethodArgumentNotValidException e) {
        final ErrorResult errorResult = new ErrorResult();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        errorResult.addError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        return ResponseEntity.badRequest()
                .body(errorResult);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> handleException(final IllegalArgumentException e) {
        return createResponseWithMessage(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResult> handleAuthenticationException() {
        return createResponseWithMessage(AuthenticationMessages.AUTHENTICATION_FAILED);
    }

    private ResponseEntity<ErrorResult> createResponseWithMessage(final String message) {
        final ErrorResult errorResult = new ErrorResult();
        errorResult.addError(null, message);

        return ResponseEntity.badRequest()
                .body(errorResult);
    }
}
