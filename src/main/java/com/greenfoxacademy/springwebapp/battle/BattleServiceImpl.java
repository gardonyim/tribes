package com.greenfoxacademy.springwebapp.battle;

import com.greenfoxacademy.springwebapp.battle.models.BattleDetails;
import com.greenfoxacademy.springwebapp.battle.models.BattleReqDTO;
import com.greenfoxacademy.springwebapp.battle.models.BattleResDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BattleServiceImpl implements BattleService {

  private KingdomService kingdomService;
  private LocationService locationService;

  public BattleServiceImpl(KingdomService kingdomService, LocationService locationService) {
    this.kingdomService = kingdomService;
    this.locationService = locationService;
  }

  @Override
  public BattleResDTO battle(Kingdom attacker, Integer defenderId, BattleReqDTO reqDTO) {
    BattleDetails battleDetails = prepareBattleDetails(attacker, defenderId, reqDTO);
    battleDetails = battlePreparation(battleDetails);
    battleDetails = battleExecution(battleDetails);
    battleTermination(battleDetails);
    return new BattleResDTO();
  }

  public BattleDetails prepareBattleDetails(Kingdom attacker, Integer defenderId, BattleReqDTO reqDTO) {
    Kingdom defender = kingdomService.findById(defenderId);
    List<Troop> attackerClones = reqDTO.getTroopIds().stream()
        .map(id -> attacker.getTroops().stream().filter(t -> t.getId() == id).findFirst().orElse(null))
        .filter(Objects::nonNull)
        .map(Troop::new)
        .collect(Collectors.toList());
    List<Troop> defenderClones = defender.getTroops().stream()
        .map(Troop::new)
        .collect(Collectors.toList());
    int distance = locationService.findShortestPath(attacker.getLocation(), defender.getLocation()).size() - 1;
    int townhallLevel = defender.getBuildings().stream()
        .filter(b -> b.getBuildingType().equals(BuildingType.TOWNHALL))
        .findFirst()
        .orElse(null)
        .getLevel();
    return new BattleDetails(attacker, defender, attackerClones, defenderClones, distance, townhallLevel);
  }

  @Override
  public BattleDetails battlePreparation(BattleDetails battleDetails) {
    return null;
  }

  @Override
  public BattleDetails battleExecution(BattleDetails battleDetails) {
    return null;
  }

  @Override
  public void battleTermination(BattleDetails battleDetails) {}

}
