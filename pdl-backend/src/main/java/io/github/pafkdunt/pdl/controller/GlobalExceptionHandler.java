package io.github.pafkdunt.pdl.controller;

import io.github.pafkdunt.pdl.exception.NotFoundException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
    return ResponseEntity.status(404).body(Map.of(
        "error", "not_found",
        "message", e.getMessage()
    ));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.status(400).body(Map.of(
        "error", "illegal_argument",
        "message", e.getMessage()
    ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(Exception e) {
    return ResponseEntity.status(500).body(Map.of(
        "error", "internal_error",
        "message", "Unexpected server error"
    ));
  }
}
