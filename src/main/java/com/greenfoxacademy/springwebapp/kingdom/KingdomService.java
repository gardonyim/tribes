package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;

public interface KingdomService {

  Kingdom save(String kingdomName, Player player);

  KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom);

  KingdomResWrappedDTO convertToKingdomResWrappedDTO(Kingdom kingdom);

}
