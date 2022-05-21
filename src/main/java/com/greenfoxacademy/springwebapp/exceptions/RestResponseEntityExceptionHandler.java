package com.greenfoxacademy.springwebapp.exceptions;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RequestParameterMissingException.class)
  public ResponseEntity handleMissingParameter(RequestParameterMissingException e) {
    return ResponseEntity.status(400).body(new ErrorDTO(e.getMessage()));
  }


  @ExceptionHandler(RequestedForbiddenResourceException.class)
  public ResponseEntity<ErrorDTO> handleForbiddenRequest(RequestedForbiddenResourceException e) {
    return ResponseEntity.status(403).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(RequestedResourceNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleResourceNotFound(RequestedResourceNotFoundException e) {
    return ResponseEntity.status(404).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(RequestNotAcceptableException.class)
  public ResponseEntity handleNotAcceptableRequest(RequestNotAcceptableException e) {
    return ResponseEntity.status(406).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler({RequestCauseConflictException.class, NotEnoughResourceException.class})
  public ResponseEntity handleConflictCausedByRequest(RuntimeException e) {
    return ResponseEntity.status(409).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(ForbiddenActionException.class)
  public ResponseEntity handleConflictCausedByPlayerForbiddenForBuilding(ForbiddenActionException e) {
    return ResponseEntity.status(403).body(new ErrorDTO(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> exception(Exception ex) {
    return ResponseEntity.status(500).body(new ErrorDTO("Unknown error"));
  }
}
