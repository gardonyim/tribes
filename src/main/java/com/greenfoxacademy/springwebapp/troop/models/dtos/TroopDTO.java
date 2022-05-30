package com.greenfoxacademy.springwebapp.troop.models.dtos;

import java.util.Objects;

public class TroopDTO {
  private Integer id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private Long startedAt;
  private Long finishedAt;

  public TroopDTO() {
  }

  public TroopDTO(
      Integer id, Integer level, Integer hp, Integer attack, Integer defence, Long startedAt, Long finishedAt) {
    this.id = id;
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

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

  public Long getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Long startedAt) {
    this.startedAt = startedAt;
  }

  public Long getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(Long finishedAt) {
    this.finishedAt = finishedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TroopDTO troopDTO = (TroopDTO) o;
    return Objects.equals(id, troopDTO.id)
        && Objects.equals(level, troopDTO.level)
        && Objects.equals(hp, troopDTO.hp)
        && Objects.equals(attack, troopDTO.attack)
        && Objects.equals(defence, troopDTO.defence)
        && Objects.equals(startedAt, troopDTO.startedAt)
        && Objects.equals(finishedAt, troopDTO.finishedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, level, hp, attack, defence, startedAt, finishedAt);
  }
}