package com.greenfoxacademy.springwebapp.exceptions;

public class RequestParameterMissingException extends RuntimeException {

  public RequestParameterMissingException(String message) {
    super(message);
  }

}
