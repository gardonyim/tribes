package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
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
    Player player = playerRepository.save(convert(reqDTO));
    player.setKingdom(kingdomService.save(reqDTO.getKingdomname(), player));
    return new RegistrationResDTO(player);
  }

  private Player convert(RegistrationReqDTO reqDTO) {
    Player player = new Player();
    player.setUsername(reqDTO.getUsername());
    player.setPassword(pwEnc.encode(reqDTO.getPassword()));
    player.setKingdom(null);
    player.setAvatar("");
    player.setPoints(0);
    return player;
  }
}
