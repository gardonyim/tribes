package com.greenfoxacademy.springwebapp.resource.models;

import java.util.Objects;

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

  public ResourceDTO(ResourceType type, int amount, int generation, long updatedAt) {
    this.type = type.name().toLowerCase();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    } else {
      ResourceDTO that = (ResourceDTO) o;
      return amount == that.amount
          && generation == that.generation
          && updatedAt == that.updatedAt
          && Objects.equals(type, that.type);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, amount, generation, updatedAt);
  }
}
