package com.greenfoxacademy.springwebapp.player.models;

public class RegistrationResDTO {

  private int id;
  private String username;
  private int kingdomId;

  public RegistrationResDTO(int id, String username, int kingdomId) {
    this.id = id;
    this.username = username;
    this.kingdomId = kingdomId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getKingdomId() {
    return kingdomId;
  }

  public void setKingdomId(int kingdomId) {
    this.kingdomId = kingdomId;
  }
}
