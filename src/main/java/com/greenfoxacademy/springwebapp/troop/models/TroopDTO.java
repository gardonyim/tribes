package com.greenfoxacademy.springwebapp.troop.models;

public class TroopDTO {

  private Integer id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private long startedAt;
  private long finishedAt;

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

  public long getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(long startedAt) {
    this.startedAt = startedAt;
  }

  public long getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(long finishedAt) {
    this.finishedAt = finishedAt;
  }
}
