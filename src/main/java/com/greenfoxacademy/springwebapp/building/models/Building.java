package com.greenfoxacademy.springwebapp.building.models;

<<<<<<< HEAD
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.time.LocalDateTime;
=======
>>>>>>> f951a4494fd1c894b52377851e09ee94280121d1
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<<<<<<< HEAD
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

=======
import lombok.Data;

@Data
>>>>>>> f951a4494fd1c894b52377851e09ee94280121d1
@Entity(name = "buildings")
public class Building {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private BuildingType buildingType;
  private int level;
  private int hp;
<<<<<<< HEAD
  @ManyToOne
  @JoinColumn(name = "kingdom_id", referencedColumnName = "id")
  private Kingdom kingdom;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  public Building(BuildingType buildingType, int level,
                  Kingdom kingdom, LocalDateTime startedAt, LocalDateTime finishedAt) {
    this.buildingType = buildingType;
    this.level = level;
    this.hp = level * buildingType.getHpParameter();
    this.kingdom = kingdom;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public Building() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public BuildingType getBuildingType() {
    return buildingType;
  }

  public void setBuildingType(BuildingType buildingType) {
    this.buildingType = buildingType;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
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
=======
  private int startedAt;
  private int finishedAt;
>>>>>>> f951a4494fd1c894b52377851e09ee94280121d1
}