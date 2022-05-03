package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.TroopDTO;

public interface TroopService {

  Troop addNewTroop(Troop troop);

  TroopDTO convertToTroopDTO(Troop troop);
}
