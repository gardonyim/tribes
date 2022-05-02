package com.greenfoxacademy.springwebapp.exceptions;


public class BuildingTypeException extends Exception {
  public BuildingTypeException() {
    super("Not a valid academy id");
  }
}