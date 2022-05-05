package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.WrongIdException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.dtos.KingdomPostDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;

public interface KingdomService {

  public Kingdom save(String kingdomName, Player player);

  void checkResources(Building building, int level) throws NotEnoughResourceException;

  void checkBuildingType(Building building) throws RequestNotAcceptableException;

  void checkOwner(Building building, Integer kingdomId);

  void checkInputParameters(KingdomPostDTO kingdomPostDTO);

  void checkBuildingId(Building building, Player player) throws WrongIdException;

}
