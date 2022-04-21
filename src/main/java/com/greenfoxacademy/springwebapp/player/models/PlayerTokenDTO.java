package com.greenfoxacademy.springwebapp.player.models;

public class PlayerTokenDTO {
  private int id;
  private String username;
  private int kingdomId;
  private String kingdomName;

  public PlayerTokenDTO() {
  }

  public PlayerTokenDTO(int id, String username, int kingdomId, String kingdomName) {
    this.id = id;
    this.username = username;
    this.kingdomId = kingdomId;
    this.kingdomName = kingdomName;

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

  public String getKingdomName() {
    return kingdomName;
  }

  public void setKingdomName(String kingdomName) {
    this.kingdomName = kingdomName;
  }

}
