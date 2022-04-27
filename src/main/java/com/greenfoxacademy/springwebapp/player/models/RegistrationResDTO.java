package com.greenfoxacademy.springwebapp.player.models;

public class RegistrationResDTO {

  private int id;
  private String username;
  private int kingdomId;
  private String avatar;
  private Integer points;

  public RegistrationResDTO(Player player) {
    this.id = player.getId();
    this.username = player.getUsername();
    this.kingdomId = player.getKingdom().getId();
    this.avatar = player.getAvatar();
    this.points = player.getPoints();
  }

  //  public RegistrationResDTO(int id, String username, int kingdomId) {
  //    this.id = id;
  //    this.username = username;
  //    this.kingdomId = kingdomId;
  //  }

  public RegistrationResDTO() {
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

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }
}
