package org.ironhack.project.eventmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BadRequestException.class,
            UnauthorizedException.class,
            NotFoundException.class,
            ForbiddenException.class
    })
    public ResponseEntity<ErrorResponse> handleKnown(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof BadRequestException) status = HttpStatus.BAD_REQUEST;
        else if (ex instanceof UnauthorizedException) status = HttpStatus.UNAUTHORIZED;
        else if (ex instanceof NotFoundException) status = HttpStatus.NOT_FOUND;
        else if (ex instanceof ForbiddenException) status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), "Database constraint violation or conflict"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), msg));
    }
}
