package com.greenfoxacademy.springwebapp.exceptions;

public class RequestedForbiddenResourceException extends RuntimeException {

  public RequestedForbiddenResourceException(String message) {
    super(message);
  }
}
