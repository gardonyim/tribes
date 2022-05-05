package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;

import java.util.List;

public interface TroopService {

  Troop createTroopOfLevel(int level, Kingdom kingdom);

  List<Troop> getTroopsOfKingdom(Integer kingdomId);

  List<TroopDTO> mapTroopsToTroopDTO(List<Troop> troopsByKingdom);

  TroopDTO convert(Troop troop);

  TroopDTO saveTroop(Kingdom kingdom, TroopPostDTO troopPostDTO);

}
