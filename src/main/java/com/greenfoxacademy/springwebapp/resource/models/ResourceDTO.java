package com.greenfoxacademy.springwebapp.resource.models;

public class ResourceDTO {
  private String type;
  private int amount;
  private int generation;
  private long updatedAt;

  public ResourceDTO() {
  }

  public ResourceDTO(String type, int amount, int generation, long updatedAt) {
    this.type = type;
    this.amount = amount;
    this.generation = generation;
    this.updatedAt = updatedAt;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getGeneration() {
    return generation;
  }

  public void setGeneration(int generation) {
    this.generation = generation;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }
}
