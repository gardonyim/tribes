package com.greenfoxacademy.springwebapp.exceptions;

public class NoUsernameException extends ParameterMissingException {

  public NoUsernameException() {
    super("Username is required.");
  }
}
