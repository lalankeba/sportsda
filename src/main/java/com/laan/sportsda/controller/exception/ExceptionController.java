package com.laan.sportsda.controller.exception;

import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(ElementNotFoundException.class)
    public ProblemDetail onElementNotFoundException(ElementNotFoundException exception) {
        log.error("ElementNotFoundException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ProblemDetail onDuplicateElementException(DuplicateElementException exception) {
        log.error("DuplicateElementException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ProblemDetail onInvalidRequestException(InvalidRequestException exception) {
        log.error("InvalidRequestException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ProblemDetail onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Optional<FieldError> optionalFieldError = exception.getBindingResult().getFieldErrors().stream().findFirst();
        String message = exception.getMessage();
        if (optionalFieldError.isPresent()) {
            message = optionalFieldError.get().getDefaultMessage();
            message = (message == null) ? "" : message;
        }
        log.error("MethodArgumentNotValidException occurred. {}", message);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail onHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("HttpRequestMethodNotSupportedException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail onObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        log.error("ObjectOptimisticLockingFailureException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail onHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail onAuthenticationException(AuthenticationException exception) {
        log.error("AuthenticationException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail onHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error("HttpMediaTypeNotSupportedException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail onException(Exception exception) {
        log.error("Exception occurred.", exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

}
