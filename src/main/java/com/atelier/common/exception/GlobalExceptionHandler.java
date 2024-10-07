package com.atelier.common.exception;

import com.atelier.common.util.ApiResponse;
import com.atelier.common.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle generic exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
        return ResponseUtils.createResponse(null, "Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle validation errors (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseUtils.createResponse(errors, "Validation Error", HttpStatus.BAD_REQUEST);
    }

    // Handle constraint violations (400 Bad Request)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return ResponseUtils.createResponse(errors, "Validation Error", HttpStatus.BAD_REQUEST);
    }

    // Handle specific bad request errors (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseUtils.createResponse(null, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle SQL errors (500 Internal Server Error)
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<?>> handleSQLException(SQLException ex) {
        return ResponseUtils.createResponse(null, "Database Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle null pointer exceptions (500 Internal Server Error)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<?>> handleNullPointerException(NullPointerException ex) {
        return ResponseUtils.createResponse(null, "Null Pointer Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle runtime exceptions (500 Internal Server Error)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        return ResponseUtils.createResponse(null, "Runtime Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle HTTP message not readable (400 Bad Request)
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return ResponseUtils.createResponse(null, "Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    // Handle missing servlet request parameters (400 Bad Request)
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(org.springframework.web.bind.MissingServletRequestParameterException ex) {
        return ResponseUtils.createResponse(null, "Missing request parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    // Handle unsupported media type (415 Unsupported Media Type)
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMediaTypeNotSupportedException(org.springframework.web.HttpMediaTypeNotSupportedException ex) {
        return ResponseUtils.createResponse(null, "Unsupported media type: " + ex.getContentType(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // Handle method not allowed (405 Method Not Allowed)
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        return ResponseUtils.createResponse(null, "Method not allowed: " + ex.getMethod(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handle access denied (403 Forbidden)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
        return ResponseUtils.createResponse(null, "Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Handle bad credentials (401 Unauthorized)
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(org.springframework.security.authentication.BadCredentialsException ex) {
        return ResponseUtils.createResponse(null, "Invalid credentials: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Handle authentication exceptions (401 Unauthorized)
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex) {
        return ResponseUtils.createResponse(null, "Authentication error: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Handle connection exceptions (503 Service Unavailable)
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ApiResponse<?>> handleConnectException(ConnectException ex) {
        return ResponseUtils.createResponse(null, "Service Unavailable: Unable to connect to service", HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Handle socket timeout exceptions (503 Service Unavailable)
    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<ApiResponse<?>> handleSocketTimeoutException(SocketTimeoutException ex) {
        return ResponseUtils.createResponse(null, "Service Unavailable: Connection timed out", HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Handle not implemented (501 Not Implemented)
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiResponse<?>> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return ResponseUtils.createResponse(null, "Not Implemented: " + ex.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(io.jsonwebtoken.JwtException ex) {
        return ResponseUtils.createResponse(null, "Invalid or malformed JWT token: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
