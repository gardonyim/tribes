package com.greenfoxacademy.springwebapp.player.models;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.login.models.Login;

import javax.persistence.*;

@Entity(name = "players")
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(length = 50)
  private String username;
  @OneToOne(mappedBy = "player")
  private Kingdom kingdom;
  private String avatar;
  private Integer points;
  @OneToOne(mappedBy = "player")
  private Login login;

  public Player(String username, String password, Kingdom kingdom, String avatar, Integer points) {
    this.username = username;
    this.kingdom = kingdom;
    this.avatar = avatar;
    this.points = points;
  }

  public Player() {
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

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
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
