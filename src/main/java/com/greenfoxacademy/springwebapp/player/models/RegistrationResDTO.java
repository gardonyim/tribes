package com.greenfoxacademy.springwebapp.player.models;

public class RegistrationResDTO {

  private int id;
  private String username;
  private int kingdomId;

  public RegistrationResDTO(Player player) {
    this.id = player.getId();
    this.username = player.getUsername();
    this.kingdomId = player.getKingdom().getId();
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
