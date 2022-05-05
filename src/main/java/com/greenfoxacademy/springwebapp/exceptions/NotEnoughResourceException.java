package com.greenfoxacademy.springwebapp.exceptions;

public class NotEnoughResourceException extends RuntimeException {
  public NotEnoughResourceException() {
    super("Not enough resource");
  }
}
