package com.greenfoxacademy.springwebapp.troop.models;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "troops")
public class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  @ManyToOne
  @JoinColumn(name = "kingdom_id")
  private Kingdom kingdom;

  public Troop() {
  }

  public Troop(Integer level, Integer hp, Integer attack, Integer defence,
               LocalDateTime startedAt, LocalDateTime finishedAt, Kingdom kingdom) {
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
    this.kingdom = kingdom;
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

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }
}
