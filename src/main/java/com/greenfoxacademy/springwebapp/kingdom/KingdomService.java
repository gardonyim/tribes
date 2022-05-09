package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;

public interface KingdomService {

  Kingdom save(String kingdomName, Player player);

  void checkKingdomPutDto(KingdomPutDTO kingdomPutDTO);

  KingdomDTO renameKingdom(Kingdom kingdom, String newKingdomName);

  KingdomDTO convert(Kingdom kingdom);
}
