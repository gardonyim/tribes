package com.greenfoxacademy.springwebapp.exceptions;

public class ForbiddenRequestException extends RuntimeException {

  public ForbiddenRequestException(String message) {
    super(message);
  }
}