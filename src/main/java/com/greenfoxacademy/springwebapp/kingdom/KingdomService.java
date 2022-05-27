package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.security.core.Authentication;

public interface KingdomService {

  Kingdom save(String kingdomName, Player player);

  Kingdom findById(Integer kingdomId);

  void checkKingdomPutDto(KingdomPutDTO kingdomPutDTO);

  KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom);

  KingdomResWrappedDTO convertToKingdomResWrappedDTO(Kingdom kingdom);

  KingdomResFullDTO fetchKingdomData(Kingdom kingdom);

  KingdomResWrappedDTO fetchKingdomData(Integer kingdomId);

  KingdomResFullDTO renameKingdom(Kingdom kingdom, KingdomPutDTO kingdomPutDTO, Authentication auth);
}
