package com.laan.sportsda.controller.exception;

import com.laan.sportsda.exception.DuplicateElementException;
import com.laan.sportsda.exception.ElementNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(ElementNotFoundException.class)
    public ProblemDetail onElementNotFoundException(ElementNotFoundException exception) {
        logger.error("ElementNotFoundException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ProblemDetail onDuplicateElementException(DuplicateElementException exception) {
        logger.error("DuplicateElementException occurred. {}", exception.getMessage());
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
        logger.error("MethodArgumentNotValidException occurred. {}", message);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail onHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        logger.error("HttpRequestMethodNotSupportedException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail onObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
        logger.error("ObjectOptimisticLockingFailureException occurred. ", exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
