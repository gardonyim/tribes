package com.greenfoxacademy.springwebapp.troop.dtos;

import java.time.LocalDateTime;

public class TroopDTO {
  private Integer id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Integer getHp() {
    return hp;
  }

  public void setHp(Integer hp) {
    this.hp = hp;
  }

  public Integer getAttack() {
    return attack;
  }

  public void setAttack(Integer attack) {
    this.attack = attack;
  }

  public Integer getDefence() {
    return defence;
  }

  public void setDefence(Integer defence) {
    this.defence = defence;
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public LocalDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(LocalDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }
}
