package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;

public interface TroopService {

  Troop addNewTroop(Troop troop);

  TroopDTO saveAndGetTroopAsDTO(int level, Kingdom kingdom);
}
