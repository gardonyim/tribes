package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;

import java.util.List;

public interface TroopService {

  Troop createTroopOfLevel(int level, Kingdom kingdom);

  TroopsDTO getTroopsOfKingdom(Kingdom kingdom);

  TroopDTO convert(Troop troop);

  List<TroopDTO> convert(List<Troop> troops);

  TroopDTO saveTroop(Kingdom kingdom, TroopPostDTO troopPostDTO);

  TroopDTO fetchTroop(Kingdom kingdom, int id);

  Troop getTroopById(Kingdom kingdom, int id);

  TroopDTO upgradeTroop(Kingdom kingdom, int troopId, TroopPostDTO dto);

  void checkInputParameters(TroopPostDTO troopPostDTO);

  Building getAcademy(Kingdom kingdom, TroopPostDTO troopPostDTO);

  Troop setValues(Troop troop, int currentLevel, int desiredLevel);

}
