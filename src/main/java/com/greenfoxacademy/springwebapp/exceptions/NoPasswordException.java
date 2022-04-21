package com.greenfoxacademy.springwebapp.exceptions;

public class NoPasswordException extends ParameterMissingException {

  public NoPasswordException() {
    super("Password is required.");
  }
}
