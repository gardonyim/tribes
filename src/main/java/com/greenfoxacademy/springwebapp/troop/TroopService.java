package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;

public interface TroopService {

  Troop createTroopOfLevel(int level, Kingdom kingdom);

  TroopsDTO getTroopsOfKingdom(Kingdom kingdom);

  TroopDTO convert(Troop troop);

  TroopDTO saveTroop(Kingdom kingdom, TroopPostDTO troopPostDTO);

}
