package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;

  @Autowired
  public KingdomServiceImpl(KingdomRepository kingdomRepository) {
    this.kingdomRepository = kingdomRepository;
  }

  @Override
  public Kingdom save(String kingdomname, Player player) {
    if (kingdomname == null || kingdomname.isEmpty()) {
      kingdomname = player.getUsername() + "'s kingdom";
    }
    return kingdomRepository.save(new Kingdom(kingdomname, player));
  }

}
