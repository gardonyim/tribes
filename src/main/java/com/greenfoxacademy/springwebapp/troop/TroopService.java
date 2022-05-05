package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TroopService {

  Troop addNewTroop(Troop troop);

  ResponseEntity<TroopDTO> saveAndGetTroopAsDTO(int level, Kingdom kingdom);

  List<Troop> getTroopsOfKingdom(Integer kingdomId);

  List<TroopDTO> mapTroopsToTroopDTO(List<Troop> troopsByKingdom);
}
