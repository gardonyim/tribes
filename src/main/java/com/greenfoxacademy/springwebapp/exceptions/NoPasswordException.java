package com.greenfoxacademy.springwebapp.exceptions;

public class NoPasswordException extends RuntimeException {

  public NoPasswordException() {
    super("Password is required.");
  }
}
