package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.exceptions.BuildingDoesNotBelongToPlayerException;
import com.greenfoxacademy.springwebapp.exceptions.BuildingTypeException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.dtos.KingdomPostDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.troop.models.Troop;

import java.util.List;

public interface KingdomService {

  public Kingdom save(String kingdomName, Player player);

  List<Troop> getTroopsOfKingdom(Integer kingdomId);

  void checkResources(Building building, int level) throws NotEnoughResourceException;

  void checkBuildingType(Building building) throws BuildingTypeException;

  void checkOwner(Building building, Integer kingdomId) throws BuildingDoesNotBelongToPlayerException;

  void checkInputParameters(KingdomPostDTO kingdomPostDTO, String jwtToken);

}
