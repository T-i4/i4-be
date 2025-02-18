package com.business.i4_be.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getStatus(), e.getMessage());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    StringBuilder sb = new StringBuilder();
    e.getBindingResult().getFieldErrors()
        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
        .forEach(message -> sb.append(message).append("\n"));

    String errorMessages = sb.toString();
    ErrorResponse errorResponse = new ErrorResponse(0,BAD_REQUEST.value(), errorMessages);
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }
}
