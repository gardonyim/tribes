package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;

public interface KingdomService {

  Kingdom save(String kingdomName, Player player);

  void checkKingdomPutDto(KingdomPutDTO kingdomPutDTO);

  KingdomResFullDTO renameKingdom(Kingdom kingdom, String newKingdomName);

  KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom);
}
