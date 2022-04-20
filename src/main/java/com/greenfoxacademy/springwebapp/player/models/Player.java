package com.greenfoxacademy.springwebapp.player.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "players")
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String username;
  private String email;
  private Integer kingdomId;
  private String avatar;
  private Integer points;

  public Player() {
  }

  public Player(String username, String email, Integer kingdomId, String avatar, Integer points) {
    this.username = username;
    this.email = email;
    this.kingdomId = kingdomId;
    this.avatar = avatar;
    this.points = points;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
