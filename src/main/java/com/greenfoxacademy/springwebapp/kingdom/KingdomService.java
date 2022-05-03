package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;

public interface KingdomService {

  public Kingdom save(String kingdomName, Player player);

  public KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom);

  public KingdomResWrappedDTO convertToKingdomResWrappedDTO(Kingdom kingdom);
}
