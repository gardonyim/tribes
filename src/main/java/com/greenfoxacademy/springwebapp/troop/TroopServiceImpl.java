package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
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
    int hp = level * gameObjectRuleHolder.getHpMultiplier("troop", level);
    long finishedAtSec = level * gameObjectRuleHolder.getBuildingTimeMultiplier("troop", level);
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
  public List<Troop> getTroopsOfKingdom(Integer kingdomId) {
    return troopRepository.findTroopsByKingdomId(kingdomId);
  }

  @Override
  public List<TroopDTO> mapTroopsToTroopDTO(List<Troop> troopsByKingdom) {
    return troopsByKingdom.stream().map(this::convert).collect(Collectors.toList());
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
  public TroopDTO saveTroop(Kingdom kingdom, TroopPostDTO troopPostDTO) {
    checkInputParameters(troopPostDTO);
    Building building = buildingService.getBuildingById(troopPostDTO.getBuildingId());
    buildingService.checkOwner(building, kingdom.getId());

    int level = building.getLevel();
    int price = gameObjectRuleHolder.getBuildingCostMultiplier("troop", level);
    resourceService.hasEnoughGold(kingdom, price);
    Troop troop = createTroopOfLevel(level, kingdom);
    Troop savedTroop = troopRepository.save(troop);
    return convert(savedTroop);
  }

  public void checkInputParameters(TroopPostDTO troopPostDTO) {
    if (troopPostDTO == null || troopPostDTO.getBuildingId() == null) {
      throw new RequestParameterMissingException("buildingId must be present");
    }
  }

}
