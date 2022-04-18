package com.greenfoxacademy.springwebapp.exceptions;

public class ShortPasswordException extends RuntimeException {

  public ShortPasswordException() {
    super("Password must be at least 8 characters.");
  }
}
