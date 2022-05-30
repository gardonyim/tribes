package com.greenfoxacademy.springwebapp.exceptions;

public class EmailSendingException  extends RuntimeException {
  public EmailSendingException() {
    super("There was a problem sending your activation email");
  }
}
