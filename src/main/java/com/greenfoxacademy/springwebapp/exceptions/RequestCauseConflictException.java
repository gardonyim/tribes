package com.greenfoxacademy.springwebapp.exceptions;

public class RequestCauseConflictException extends RuntimeException {

  public RequestCauseConflictException(String message) {
    super(message);
  }
}
