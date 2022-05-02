package com.greenfoxacademy.springwebapp.exceptions;

public class BuildingDoesNotBelongToPlayerException extends Throwable {
  public BuildingDoesNotBelongToPlayerException() {
    super("Not a valid academy id");
  }

  public BuildingDoesNotBelongToPlayerException(String message, Integer id) {
    super(message + " " + id);
  }
}
