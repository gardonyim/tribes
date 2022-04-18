package com.greenfoxacademy.springwebapp.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

  public UsernameAlreadyExistsException() {
    super("Username is already taken.");
  }
}
