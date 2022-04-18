package com.greenfoxacademy.springwebapp.exceptions;

public class NoUsernameAndPasswordException extends RuntimeException {

  public NoUsernameAndPasswordException() {
    super("Username and password are required.");
  }
}
