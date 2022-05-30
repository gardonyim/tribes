package com.greenfoxacademy.springwebapp.exceptions;

public class RequestedResourceNotFoundException extends RuntimeException {

  public RequestedResourceNotFoundException(String message) {
    super(message);
  }
}
