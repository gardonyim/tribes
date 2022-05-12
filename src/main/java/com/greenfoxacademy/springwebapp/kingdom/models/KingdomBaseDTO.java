package com.greenfoxacademy.springwebapp.kingdom.models;

import com.google.common.base.Objects;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;

public class KingdomBaseDTO {
  private int kingdomId;
  private String name;
  private int userId;
  private LocationDTO location;

  public KingdomBaseDTO() {
  }

  public KingdomBaseDTO(int kingdomId, String name, int userId, LocationDTO location) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof KingdomBaseDTO)) return false;
    KingdomBaseDTO that = (KingdomBaseDTO) o;
    return kingdomId == that.kingdomId && userId == that.userId && Objects.equal(name, that.name) && Objects.equal(location, that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(kingdomId, name, userId, location);
  }
}