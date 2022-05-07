package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;

public interface KingdomService {

<<<<<<< HEAD
  Kingdom save(String kingdomName, Player player);

  KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom);

  KingdomResWrappedDTO convertToKingdomResWrappedDTO(Kingdom kingdom);

=======
  public Kingdom save(String kingdomName, Player player);

  public KingdomResFullDTO convertToKingdomResFullDTO(Kingdom kingdom);

  public KingdomResWrappedDTO convertToKingdomResWrappedDTO(Kingdom kingdom);
>>>>>>> 91dfa9e60c954dea91aadbe4c8d9857e02414d94
}
