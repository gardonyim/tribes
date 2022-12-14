package com.greenfoxacademy.springwebapp.building.models;

public class BuildingDTO {

  private int id;
  private String type;
  private int level;
  private int hp;
  private long startedAt;
  private long finishedAt;

  public BuildingDTO() {
  }

  public BuildingDTO(int id, BuildingType buildingType, int level, int hp, long startedAt, long finishedAt) {
    this.id = id;
    this.type = buildingType.name().toLowerCase();
    this.level = level;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuildingDTO that = (BuildingDTO) o;
    if (id != that.id || level != that.level || hp != that.hp || startedAt != that.startedAt
        || finishedAt != that.finishedAt) {
      return false;
    }
    return type.equals(that.type);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + level;
    result = 31 * result + hp;
    result = 31 * result + (int) (startedAt ^ (startedAt >>> 32));
    result = 31 * result + (int) (finishedAt ^ (finishedAt >>> 32));
    return result;
  }
}
