package com.greenfoxacademy.springwebapp.location.models;

import java.io.Serializable;

public class LocationDTO implements Serializable {
  private final int x;
  private final int y;

  public LocationDTO(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
