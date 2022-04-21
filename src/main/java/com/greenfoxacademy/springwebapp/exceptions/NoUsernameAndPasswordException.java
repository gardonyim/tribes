package com.greenfoxacademy.springwebapp.exceptions;

public class NoUsernameAndPasswordException extends ParameterMissingException {

  public NoUsernameAndPasswordException() {
    super("Username and password are required.");
  }
}
