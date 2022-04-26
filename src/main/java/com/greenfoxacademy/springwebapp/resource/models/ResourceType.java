package com.greenfoxacademy.springwebapp.resource.models;

public enum ResourceType {
  FOOD("food"),
  GOLD("gold");

  private final String description;

  ResourceType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}