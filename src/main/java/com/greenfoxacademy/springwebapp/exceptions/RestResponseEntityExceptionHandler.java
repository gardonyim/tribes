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

  @ExceptionHandler({RequestNotAcceptableException.class})
  public ResponseEntity handleNotAcceptableRequest(RuntimeException e) {
    return ResponseEntity.status(406).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler({RequestCauseConflictException.class, NotEnoughResourceException.class})
  public ResponseEntity handleConflictCausedByRequest(RuntimeException e) {
    return ResponseEntity.status(409).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(WrongIdException.class)
  public ResponseEntity handleConflictCausedByPlayerForbiddenForBuilding(WrongIdException e) {
    return ResponseEntity.status(403).body(new ErrorDTO(e.getMessage()));
  }
}