package com.greenfoxacademy.springwebapp.exceptions;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RequestParameterMissingException.class)
  public ResponseEntity handleMissingParameter(RequestParameterMissingException e) {
    return ResponseEntity.status(400).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(RequestNotAcceptableException.class)
  public ResponseEntity handleNotAcceptableRequest(RequestNotAcceptableException e) {
    return ResponseEntity.status(406).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(RequestCauseConflictException.class)
  public ResponseEntity handleConflictCausedByRequest(RequestCauseConflictException e) {
    return ResponseEntity.status(409).body(new ErrorDTO(e.getMessage()));
  }

}