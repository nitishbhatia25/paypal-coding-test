package com.paypal.bfs.test.employeeserv.errorhandling;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity handleException(DataIntegrityViolationException ex) {
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Missing Data In Request Payload",ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity handleException(IllegalArgumentException ex) {
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid field in the request payload", ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleException(Exception ex) {
    final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error in processing request", ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity handleException(MissingRequestHeaderException ex) {
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Missing request header", ex.getLocalizedMessage());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

}
