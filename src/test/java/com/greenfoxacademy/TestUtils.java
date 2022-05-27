package com.greenfoxacademy;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomBaseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;

import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

  public static LocationDtoBuilder locationDtoBuilder(Location location) {
    return new LocationDtoBuilder()
        .withX(location.getxcoordinate())
        .withY(location.getycoordinate());
  }

  public static TroopDtoBuilder troopDtoBuilder(Troop troop) {
    return new TroopDtoBuilder()
        .withId(troop.getId())
        .withLevel(troop.getLevel())
        .withHp(troop.getHp())
        .withAttack(troop.getAttack())
        .withDefence(troop.getDefence())
        .withStartedAt(troop.getStartedAt().toEpochSecond(ZoneOffset.UTC))
        .withFinishedAt(troop.getFinishedAt().toEpochSecond(ZoneOffset.UTC));
  }

  public static BuildingDtoBuilder buildingDtoBuilder(Building building) {
    return new BuildingDtoBuilder()
        .withId(building.getId())
        .withBuildingType(building.getBuildingType().getName().toLowerCase())
        .withLevel(building.getLevel())
        .withHp(building.getHp())
        .withStartedAt(building.getStartedAt().toEpochSecond(ZoneOffset.UTC))
        .withFinishedAt(building.getFinishedAt().toEpochSecond(ZoneOffset.UTC));

  }

  public static ResourceDtoBuilder resourceDtoBuilder(Resource resource) {
    return new ResourceDtoBuilder()
        .withType(resource.getResourceType().getDescription().toLowerCase())
        .withAmount(resource.getAmount())
        .withGeneration(resource.getGeneration())
        .withUpdatedAt(resource.getUpdatedAt().toEpochSecond(ZoneOffset.UTC));
  }

  public static KingdomBaseDtoBuilder kingdomBaseDtoBuilder(Kingdom kingdom) {
    return new KingdomBaseDtoBuilder()
        .withKingdomId(kingdom.getId())
        .withName(kingdom.getName())
        .withUserId(kingdom.getPlayer().getId())
        .withLocationDTO(locationDtoBuilder(kingdom.getLocation()).build());
  }

  public static KingdomResFullDtoBuilder kingdomResFullDtoBuilder(Kingdom kingdom) {
    return new KingdomResFullDtoBuilder()
        .withKingdomId(kingdom.getId())
        .withName(kingdom.getName())
        .withUserId(kingdom.getPlayer().getId())
        .withLocationDTO(locationDtoBuilder(kingdom.getLocation()).build())
        .withBuildingDTOs(
            kingdom.getBuildings().stream()
                .map(TestUtils::buildingDtoBuilder)
                .map(TestUtils.BuildingDtoBuilder::build).collect(Collectors.toList())
        )
        .withResourceDTOs(kingdom.getResources().stream()
            .map(TestUtils::resourceDtoBuilder)
            .map(TestUtils.ResourceDtoBuilder::build).collect(Collectors.toList())
        )
        .withTroopDTOs(kingdom.getTroops().stream()
            .map(TestUtils::troopDtoBuilder)
            .map(TestUtils.TroopDtoBuilder::build).collect(Collectors.toList())
        );
  }

  public static TroopPostDtoBuilder troopPostDtoBuilder() {
    return new TroopPostDtoBuilder()
        .withBuildingId(random(1, 10000));
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
      building.setFinishedAt(LocalDateTime.parse(finishedAt));
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

  public static class LocationDtoBuilder {
    private LocationDTO locationDTO;

    LocationDtoBuilder() {
      locationDTO = new LocationDTO();
    }

    public LocationDtoBuilder withX(int x) {
      locationDTO.setX(x);
      return this;
    }

    public LocationDtoBuilder withY(int y) {
      locationDTO.setY(y);
      return this;
    }

    public LocationDTO build() {
      return locationDTO;
    }

  }

  public static class TroopDtoBuilder {
    private TroopDTO troopDTO;

    TroopDtoBuilder() {
      troopDTO = new TroopDTO();
    }

    public TroopDtoBuilder withId(Integer id) {
      troopDTO.setId(id);
      return this;
    }

    public TroopDtoBuilder withLevel(Integer level) {
      troopDTO.setLevel(level);
      return this;
    }

    public TroopDtoBuilder withHp(Integer hp) {
      troopDTO.setHp(hp);
      return this;
    }

    public TroopDtoBuilder withAttack(Integer attack) {
      troopDTO.setAttack(attack);
      return this;
    }

    public TroopDtoBuilder withDefence(Integer defence) {
      troopDTO.setDefence(defence);
      return this;
    }

    public TroopDtoBuilder withStartedAt(Long startedAt) {
      troopDTO.setStartedAt(startedAt);
      return this;
    }

    public TroopDtoBuilder withFinishedAt(Long finishedAt) {
      troopDTO.setFinishedAt(finishedAt);
      return this;
    }

    public TroopDTO build() {
      return troopDTO;
    }

  }

  public static class BuildingDtoBuilder {
    private BuildingDTO buildingDTO;

    BuildingDtoBuilder() {
      buildingDTO = new BuildingDTO();
    }

    public BuildingDtoBuilder withId(int id) {
      buildingDTO.setId(id);
      return this;
    }

    public BuildingDtoBuilder withBuildingType(String buildingType) {
      buildingDTO.setType(buildingType);
      return this;
    }

    public BuildingDtoBuilder withLevel(int level) {
      buildingDTO.setLevel(level);
      return this;
    }

    public BuildingDtoBuilder withHp(int hp) {
      buildingDTO.setHp(hp);
      return this;
    }

    public BuildingDtoBuilder withStartedAt(long startedAt) {
      buildingDTO.setStartedAt(startedAt);
      return this;
    }

    public BuildingDtoBuilder withFinishedAt(long finishedAt) {
      buildingDTO.setFinishedAt(finishedAt);
      return this;
    }

    public BuildingDTO build() {
      return buildingDTO;
    }

  }

  public static class ResourceDtoBuilder {
    private ResourceDTO resourceDTO;

    ResourceDtoBuilder() {
      resourceDTO = new ResourceDTO();
    }

    public ResourceDtoBuilder withType(String type) {
      resourceDTO.setType(type);
      return this;
    }

    public ResourceDtoBuilder withAmount(int amount) {
      resourceDTO.setAmount(amount);
      return this;
    }

    public ResourceDtoBuilder withGeneration(int generation) {
      resourceDTO.setGeneration(generation);
      return this;
    }

    public ResourceDtoBuilder withUpdatedAt(long updatedAt) {
      resourceDTO.setUpdatedAt(updatedAt);
      return this;
    }

    public ResourceDTO build() {
      return resourceDTO;
    }

  }

  public static class KingdomBaseDtoBuilder {
    private KingdomBaseDTO kingdomBaseDTO;

    KingdomBaseDtoBuilder() {
      kingdomBaseDTO = new KingdomBaseDTO();
    }

    public KingdomBaseDtoBuilder withKingdomId(int id) {
      kingdomBaseDTO.setKingdomId(id);
      return this;
    }

    public KingdomBaseDtoBuilder withName(String name) {
      kingdomBaseDTO.setName(name);
      return this;
    }

    public KingdomBaseDtoBuilder withUserId(int userId) {
      kingdomBaseDTO.setUserId(userId);
      return this;
    }

    public KingdomBaseDtoBuilder withLocationDTO(LocationDTO locationDTO) {
      kingdomBaseDTO.setLocation(locationDTO);
      return this;
    }

    public KingdomBaseDTO build() {
      return kingdomBaseDTO;
    }
  }

  public static class KingdomResFullDtoBuilder {
    private KingdomResFullDTO kingdomResFullDTO;

    KingdomResFullDtoBuilder() {
      kingdomResFullDTO = new KingdomResFullDTO();
    }

    public KingdomResFullDtoBuilder withKingdomId(int id) {
      kingdomResFullDTO.setKingdomId(id);
      return this;
    }

    public KingdomResFullDtoBuilder withName(String name) {
      kingdomResFullDTO.setName(name);
      return this;
    }

    public KingdomResFullDtoBuilder withUserId(int userId) {
      kingdomResFullDTO.setUserId(userId);
      return this;
    }

    public KingdomResFullDtoBuilder withLocationDTO(LocationDTO locationDTO) {
      kingdomResFullDTO.setLocation(locationDTO);
      return this;
    }

    public KingdomResFullDtoBuilder withBuildingDTOs(List<BuildingDTO> buildingDTOs) {
      kingdomResFullDTO.setBuildings(buildingDTOs);
      return this;
    }

    public KingdomResFullDtoBuilder withResourceDTOs(List<ResourceDTO> resourceDTOs) {
      kingdomResFullDTO.setResources(resourceDTOs);
      return this;
    }

    public KingdomResFullDtoBuilder withTroopDTOs(List<TroopDTO> troopDTOs) {
      kingdomResFullDTO.setTroops(troopDTOs);
      return this;
    }

    public KingdomResFullDTO build() {
      return kingdomResFullDTO;
    }
  }
  
  public static class TroopPostDtoBuilder {
    private TroopPostDTO troopPostDTO;

    public TroopPostDtoBuilder(TroopPostDTO troopPostDTO) {
      this.troopPostDTO = troopPostDTO;
    }

    public TroopPostDtoBuilder() {
      this.troopPostDTO = new TroopPostDTO();
    }

    public TroopPostDtoBuilder withBuildingId(Integer buildingId) {
      troopPostDTO.setBuildingId(buildingId);
      return this;
    }

    public TroopPostDTO build() {
      return troopPostDTO;
    }
  }

  public static LocalDateTime timeOf(String time) {
    return LocalDateTime.parse(time).atZone(ZoneOffset.UTC).toLocalDateTime();
  }
}
