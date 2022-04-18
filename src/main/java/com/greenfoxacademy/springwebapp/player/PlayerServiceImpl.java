package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;
  private KingdomService kingdomService;
  private PasswordEncoder pwEnc;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository, KingdomService kingdomService, PasswordEncoder pwEnc) {
    this.playerRepository = playerRepository;
    this.kingdomService = kingdomService;
    this.pwEnc = pwEnc;
  }

  @Override
  public RegistrationResDTO savePlayer(RegistrationReqDTO reqDTO) {
    Player player = playerRepository.save(new Player(reqDTO.getUsername(),
            pwEnc.encode(reqDTO.getPassword()), null, "", 0));
    String kingdomname = reqDTO.getKingdomname();
    if (kingdomname == null || kingdomname.isEmpty()) {
      kingdomname = reqDTO.getUsername() + "'s kingdom";
    }
    Kingdom kingdom = kingdomService.save(new Kingdom(kingdomname, player));
    player.setKingdom(kingdom);
    return new RegistrationResDTO(player.getId(), player.getUsername(), kingdom.getId());
  }
}
