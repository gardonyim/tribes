package com.greenfoxacademy.springwebapp.exceptions;

public class RequestNotAcceptableException extends RuntimeException {

  public RequestNotAcceptableException(String message) {
    super(message);
  }
}
