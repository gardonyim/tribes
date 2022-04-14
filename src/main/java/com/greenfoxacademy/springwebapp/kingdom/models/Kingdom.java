package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.player.models.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "kingdoms")
public class Kingdom {

  @Id
  private int id;
  private String name;
  @OneToOne()
  private Player player;

  public Kingdom(int id, String name, Player player) {
    this.id = id;
    this.name = name;
    this.player = player;
  }

  public Kingdom() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
