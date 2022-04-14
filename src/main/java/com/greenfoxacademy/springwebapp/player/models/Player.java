package com.greenfoxacademy.springwebapp.player.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "players")
public class Player {

  @Id
  private int id;
  @Column(length = 50)
  private String username;
  private String password;
  private Integer kingdomId;
  private String avatar;
  private Integer points;

  public Player(String username, String password, Integer kingdomId, String avatar, Integer points) {
    this.username = username;
    this.password = password;
    this.kingdomId = kingdomId;
    this.avatar = avatar;
    this.points = points;
  }

  public Player() {
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getKingdomId() {
    return kingdomId;
  }

  public void setKingdomId(Integer kingdomId) {
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
