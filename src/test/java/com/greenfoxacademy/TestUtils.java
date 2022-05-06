package com.greenfoxacademy;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.troop.models.Troop;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

  public static int random(int from, int to) {
    return (int) ((Math.random() * (from - to)) + from);
  }

  public static int randomId() {
    return random(9000, 10000);
  }

  public static Location defaultLocation() {
    return new Location(randomId(), random(0, 100), random(0, 100));
  }

  public static Player defaultPlayer() {
    return playerBuilder().build();
  }

  public static Kingdom defaultKingdom() {
    return kingdomBuilder().build();
  }

  public static Resource defaultResource(ResourceType type) {
    return resourceBuilder(type).build();
  }

  public static Building defaultBuilding(BuildingType type) {
    return buildingBuilder(type).build();
  }

  public static Troop defaultTroop() {
    return troopBuilder().build();
  }

  public static TroopBuilder troopBuilder() {
    return new TroopBuilder()
            .withId(randomId())
            .withLevel(1)
            .withHP(20)
            .withAttack(10)
            .withDefence(5)
            .startedAt("2022-01-01T00:00:00")
            .finishedAt("2022-01-01T00:01:00");
  }

  public static BuildingBuilder buildingBuilder(BuildingType type) {
    return new BuildingBuilder()
            .withId(randomId())
            .withLevel(1)
            .withType(type)
            .withHP(100)
            .startedAt("2022-01-01T00:00:00")
            .finishedAt("2022-01-01T00:01:00");
  }

  public static ResourceBuilder resourceBuilder(ResourceType type) {
    return new ResourceBuilder()
            .withId(randomId())
            .withType(type)
            .withAmount(100)
            .withGeneration(10)
            .updatedAt("2022-01-01T00:00:00");
  }

  public static KingdomBuilder kingdomBuilder() {
    return new KingdomBuilder()
            .withId(randomId())
            .withName("kingdom")
            .withResources(Arrays.asList(
                    defaultResource(ResourceType.FOOD),
                    defaultResource(ResourceType.GOLD)))
            .withBuildings(Arrays.asList(
                    defaultBuilding(BuildingType.TOWNHALL),
                    defaultBuilding(BuildingType.FARM),
                    defaultBuilding(BuildingType.MINE),
                    defaultBuilding(BuildingType.ACADEMY)
            ))
            .withTroops(Arrays.asList(
                    defaultTroop()
            ));
  }

  public static PlayerBuilder playerBuilder() {
    return new PlayerBuilder()
            .withId(randomId())
            .withUsername("player")
            .withPassword("password")
            .withAvatar("")
            .withPoints(0)
            .withKingdom(defaultKingdom());
  }

  public static class PlayerBuilder {
    private Player player;

    PlayerBuilder() {
      player = new Player();
    }

    PlayerBuilder(Player player) {
      this.player = player;
    }

    public PlayerBuilder withId(int id) {
      player.setId(id);
      return this;
    }

    public PlayerBuilder withUsername(String username) {
      player.setUsername(username);
      return this;
    }

    public PlayerBuilder withPassword(String password) {
      player.setUsername(password);
      return this;
    }

    public PlayerBuilder withKingdom(Kingdom kingdom) {
      player.setKingdom(kingdom);
      return this;
    }

    public PlayerBuilder withAvatar(String avatar) {
      player.setAvatar(avatar);
      return this;
    }

    public PlayerBuilder withPoints(int points) {
      player.setPoints(points);
      return this;
    }

    public Player build() {
      return player;
    }
  }

  public static class KingdomBuilder {
    private Kingdom kingdom;

    KingdomBuilder() {
      kingdom = new Kingdom();
    }

    KingdomBuilder(Kingdom kingdom) {
      this.kingdom = kingdom;
    }

    public KingdomBuilder withId(int id) {
      kingdom.setId(id);
      return this;
    }

    public KingdomBuilder withName(String name) {
      kingdom.setName(name);
      return this;
    }

    public KingdomBuilder withBuildings(List<Building> buildings) {
      kingdom.setBuildings(buildings);
      return this;
    }

    public KingdomBuilder withResources(List<Resource> resources) {
      kingdom.setResources(resources);
      return this;
    }

    public KingdomBuilder withTroops(List<Troop> troops) {
      kingdom.setTroops(troops);
      return this;
    }

    public Kingdom build() {
      return kingdom;
    }
  }

  public static class BuildingBuilder {
    private Building building;

    BuildingBuilder() {
      building = new Building();
    }

    BuildingBuilder(Building building) {
      this.building = building;
    }

    public BuildingBuilder withId(int id) {
      building.setId(id);
      return this;
    }

    public BuildingBuilder withType(BuildingType type) {
      building.setBuildingType(type);
      return this;
    }

    public BuildingBuilder withLevel(int level) {
      building.setLevel(level);
      return this;
    }

    public BuildingBuilder withHP(int hp) {
      building.setHp(hp);
      return this;
    }

    public BuildingBuilder startedAt(String startedAt) {
      building.setStartedAt(LocalDateTime.parse(startedAt));
      return this;
    }

    public BuildingBuilder finishedAt(String finishedAt) {
      building.setStartedAt(LocalDateTime.parse(finishedAt));
      return this;
    }

    public Building build() {
      return building;
    }
  }

  public static class ResourceBuilder {
    private Resource resource;

    ResourceBuilder() {
      resource = new Resource();
    }

    ResourceBuilder(Resource resource) {
      this.resource = resource;
    }

    public ResourceBuilder withId(int id) {
      resource.setId(id);
      return this;
    }

    public ResourceBuilder withType(ResourceType type) {
      resource.setResourceType(type);
      return this;
    }

    public ResourceBuilder withAmount(int amount) {
      resource.setAmount(amount);
      return this;
    }

    public ResourceBuilder withGeneration(int generation) {
      resource.setGeneration(generation);
      return this;
    }

    public ResourceBuilder updatedAt(String updatedAt) {
      resource.setUpdatedAt(LocalDateTime.parse(updatedAt));
      return this;
    }

    public Resource build() {
      return resource;
    }
  }

  public static class TroopBuilder {
    private Troop troop;

    TroopBuilder() {
      troop = new Troop();
    }

    TroopBuilder(Troop troop) {
      this.troop = troop;
    }

    public TroopBuilder withId(int id) {
      troop.setId(id);
      return this;
    }

    public TroopBuilder withLevel(int level) {
      troop.setLevel(level);
      return this;
    }

    public TroopBuilder withHP(int hp) {
      troop.setHp(hp);
      return this;
    }

    public TroopBuilder withAttack(int attack) {
      troop.setAttack(attack);
      return this;
    }

    public TroopBuilder withDefence(int defence) {
      troop.setDefence(defence);
      return this;
    }

    public TroopBuilder startedAt(String startedAt) {
      troop.setStartedAt(timeOf(startedAt));
      return this;
    }

    public TroopBuilder finishedAt(String finishedAt) {
      troop.setFinishedAt(timeOf(finishedAt));
      return this;
    }

    public Troop build() {
      return troop;
    }
  }

  public static LocalDateTime timeOf(String time) {
    return LocalDateTime.parse(time).atZone(ZoneOffset.UTC).toLocalDateTime();
  }

}
