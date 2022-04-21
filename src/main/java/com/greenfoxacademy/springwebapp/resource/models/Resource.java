package com.greenfoxacademy.springwebapp.resource.models;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name= "resources")
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private ResourceType type;
  private int amount;
  private int generation;
  private LocalDateTime updatedAt;
  @ManyToOne
  @JoinColumn(name = "kingdom_id", referencedColumnName = "id")
  private Kingdom kingdom;

  public Resource() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ResourceType getType() {
    return type;
  }

  public void setType(ResourceType type) {
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

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }
}
