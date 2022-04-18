package com.greenfoxacademy.springwebapp.exceptions;

public class NoUsernameException extends RuntimeException {

  public NoUsernameException() {
    super("Username is required.");
  }
}
