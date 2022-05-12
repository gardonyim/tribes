package com.greenfoxacademy.springwebapp.exceptions;

public class ForbiddenActionException extends RuntimeException {

  public ForbiddenActionException() {
    super("Forbidden action");
  }

}
