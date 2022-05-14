package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TroopServiceImpl implements TroopService {

  private final BuildingService buildingService;
  private final ResourceService resourceService;
  private final GameObjectRuleHolder gameObjectRuleHolder;
  private final TroopRepository troopRepository;

  public TroopServiceImpl(BuildingService buildingService, ResourceService resourceService,
                          GameObjectRuleHolder gameObjectRuleHolder, TroopRepository troopRepository) {
    this.buildingService = buildingService;
    this.resourceService = resourceService;
    this.gameObjectRuleHolder = gameObjectRuleHolder;
    this.troopRepository = troopRepository;
  }

  @Override
  public Troop createTroopOfLevel(int level, Kingdom kingdom) {
    int hp = level * gameObjectRuleHolder.getHpMultiplier("troop", level); //TODO: change to gameObjectRuleHandler.calcNewHp()
    long finishedAtSec = level * gameObjectRuleHolder.getBuildingTimeMultiplier("troop", level); //TODO: change to .calcBuildingTime()
    Troop troop = new Troop();
    troop.setLevel(level);
    troop.setHp(hp);
    troop.setKingdom(kingdom);
    // TODO: troop attack and defence should be stored in game settings as well
    troop.setAttack(level * 10);
    troop.setDefence(level * 5);
    troop.setStartedAt(TimeService.actualTime());
    troop.setFinishedAt(TimeService.timeAtNSecondsLater(finishedAtSec));
    return troop;
  }

  @Override
  public TroopsDTO getTroopsOfKingdom(Kingdom kingdom) {
    List<TroopDTO> troops = kingdom.getTroops().stream().map(this::convert).collect(Collectors.toList());
    return new TroopsDTO(troops);
  }

  @Override
  public TroopDTO convert(Troop troop) {
    TroopDTO dto = new TroopDTO();
    dto.setId(troop.getId());
    dto.setLevel(troop.getLevel());
    dto.setHp(troop.getHp());
    dto.setAttack(troop.getAttack());
    dto.setDefence(troop.getDefence());
    dto.setStartedAt(TimeService.toEpochSecond(troop.getStartedAt()));
    dto.setFinishedAt(TimeService.toEpochSecond(troop.getFinishedAt()));
    return dto;
  }

  @Override
  public List<TroopDTO> convert(List<Troop> troops) {
    return troops.stream().map(this::convert).collect(Collectors.toList());
  }

  @Override
  public TroopDTO saveTroop(Kingdom kingdom, TroopPostDTO troopPostDTO) {
    checkInputParameters(troopPostDTO);
    Building building = buildingService.getBuildingById(troopPostDTO.getBuildingId());
    buildingService.checkOwner(building, kingdom.getId());
    if (!building.getBuildingType().equals(BuildingType.ACADEMY)) {
      throw new RequestNotAcceptableException("Not a valid academy id");
    }
    int level = building.getLevel();
    int price = gameObjectRuleHolder.getBuildingCostMultiplier("troop", level);
    resourceService.hasEnoughGold(kingdom, price);
    resourceService.pay(kingdom, price);
    resourceService.updateResourceGeneration(kingdom, "troop", 0, level);
    Troop troop = createTroopOfLevel(level, kingdom);
    Troop savedTroop = troopRepository.save(troop);
    return convert(savedTroop);
  }

  @Override
  public TroopDTO fetchTroop(Kingdom kingdom, int id) {
    return convert(getTroopById(kingdom, id));
  }

  @Override
  public Troop getTroopById(Kingdom kingdom, int id) {
    Troop troop = troopRepository.findById(id)
        .orElseThrow(() -> new RequestedResourceNotFoundException("Id not found"));
    if (troop.getKingdom().getId() != kingdom.getId()) {
      throw new ForbiddenActionException();
    }
    return troop;
  }

  @Override
  public TroopDTO upgradeTroop(Kingdom kingdom, int troopId, TroopPostDTO dto) {
    Building building = getAcademy(kingdom, dto);
    Troop troop = getTroopById(kingdom, troopId);
    int currentLevel = troop.getLevel();
    int desiredLevel = building.getLevel();
    int cost = gameObjectRuleHolder.calcCreationCost("troop", currentLevel, desiredLevel);
    if (resourceService.hasEnoughGold(kingdom, cost)) {
      resourceService.pay(kingdom, cost);
      return convert(troopRepository.save(setValues(troop, currentLevel, desiredLevel)));
    }
    throw new NotEnoughResourceException();
  }

  @Override
  public Building getAcademy(Kingdom kingdom, TroopPostDTO dto) {
    checkInputParameters(dto);
    Building building = buildingService.getBuildingById(dto.getBuildingId());
    buildingService.checkOwner(building, kingdom.getId());
    if (building.getBuildingType() != BuildingType.ACADEMY) {
      throw new RequestNotAcceptableException("Not a valid academy id");
    }
    return building;
  }

  @Override
  public Troop setValues(Troop troop, int currentLevel, int desiredLevel) {
    troop.setLevel(desiredLevel);
    troop.setHp(gameObjectRuleHolder.calcNewHP("troop", desiredLevel));
    troop.setAttack(gameObjectRuleHolder.calcNewAttack("troop", desiredLevel));
    troop.setDefence(gameObjectRuleHolder.calcNewDefence("troop", desiredLevel));
    troop.setStartedAt(TimeService.actualTime());
    int buildingTime = gameObjectRuleHolder.calcCreationTime("troop", currentLevel, desiredLevel);
    troop.setFinishedAt(TimeService.timeAtNSecondsLater(buildingTime));
    return troop;
  }

  @Override
  public void checkInputParameters(TroopPostDTO troopPostDTO) {
    if (troopPostDTO == null || troopPostDTO.getBuildingId() == null) {
      throw new RequestParameterMissingException("buildingId must be present");
    }
  }

}
