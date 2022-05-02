package com.greenfoxacademy.springwebapp.exceptions;

public class NotEnoughResourceException extends Throwable {
  public NotEnoughResourceException() {
    super("Not enough resource");
  }
}
