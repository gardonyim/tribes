package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.location.models.LocationDTO;

public class KingdomDTO {
  private int kingdomId;
  private String name;
  private int userId;
  private LocationDTO location;

  public KingdomDTO() {
  }

  public KingdomDTO(int kingdomId, String name, int userId, LocationDTO location) {
    this.kingdomId = kingdomId;
    this.name = name;
    this.userId = userId;
    this.location = location;
  }

  public int getKingdomId() {
    return kingdomId;
  }

  public void setKingdomId(int kingdomId) {
    this.kingdomId = kingdomId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public LocationDTO getLocation() {
    return location;
  }

  public void setLocation(LocationDTO location) {
    this.location = location;
  }
}