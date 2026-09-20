package com.lcwd.jwt.exception;

import com.lcwd.jwt.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Invalid credentials ───────────────────────────────────────────────────
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentials(InvalidCredentialsException ex,
                                                  HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
    }

    // ── User not found ────────────────────────────────────────────────────────
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUsernameNotFound(UsernameNotFoundException ex,
                                                HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid credentials", req.getRequestURI());
    }

    // ── JWT expired ───────────────────────────────────────────────────────────
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleExpiredJwt(ExpiredJwtException ex,
                                          HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Token has expired", req.getRequestURI());
    }

    // ── JWT malformed / invalid ───────────────────────────────────────────────
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwtException(JwtException ex,
                                            HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid JWT token", req.getRequestURI());
    }

    // ── Validation errors (@Valid) ────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex,
                                          HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, message, req.getRequestURI());
    }

    // ── Fallback ──────────────────────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", req.getRequestURI());
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private ErrorResponse build(HttpStatus status, String message, String path) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now(),
                path
        );
    }
}