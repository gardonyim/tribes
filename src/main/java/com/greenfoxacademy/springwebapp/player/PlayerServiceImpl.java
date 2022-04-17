package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;
  private KingdomService kingdomService;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository, KingdomService kingdomService) {
    this.playerRepository = playerRepository;
    this.kingdomService = kingdomService;
  }

  @Override
  public RegistrationResDTO savePlayer(RegistrationReqDTO reqDTO) {
    Player player = playerRepository.save(new Player(reqDTO.getUsername(), "", null, "", 0));
    System.out.println("Id: " + player.getId());
    String kingdomname = reqDTO.getKingdomname();
    if (kingdomname == null || kingdomname.isEmpty()) {
      kingdomname = reqDTO.getUsername() + "'s kingdom";
    }
    Kingdom kingdom = kingdomService.save(new Kingdom(kingdomname, player));
    player.setKingdom(kingdom);
    return new RegistrationResDTO(player.getId(), player.getUsername(), kingdom.getId());
  }
}
