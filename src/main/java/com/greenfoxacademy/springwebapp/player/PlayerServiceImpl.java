package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.NoPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.NoUsernameAndPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.NoUsernameException;
import com.greenfoxacademy.springwebapp.exceptions.ShortPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.UsernameAlreadyExistsException;
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
    validateRegistration(reqDTO);
    Player player = playerRepository.save(convert(reqDTO));
    player.setKingdom(kingdomService.save(reqDTO.getKingdomname(), player));
    return new RegistrationResDTO(player);
  }

  private void validateRegistration(RegistrationReqDTO reqDTO) {
    if (reqDTO.getPassword() == null || reqDTO.getPassword().trim().isEmpty()) {
      if (reqDTO.getUsername() == null || reqDTO.getUsername().trim().isEmpty()) {
        throw new NoUsernameAndPasswordException();
      } else {
        throw new NoPasswordException();
      }
    }
    if (reqDTO.getUsername() == null || reqDTO.getUsername().trim().isEmpty()) {
      throw new NoUsernameException();
    }
    
    if (playerRepository.findFirstByUsername(reqDTO.getUsername()).isPresent()) {
      throw new UsernameAlreadyExistsException();
    }

    int minPasswordLength = 8;
    if (reqDTO.getPassword().trim().length() < minPasswordLength) {
      throw new ShortPasswordException();
    }
  }

  private Player convert(RegistrationReqDTO reqDTO) {
    Player player = new Player();
    player.setUsername(reqDTO.getUsername());
    player.setPassword(pwEnc.encode(reqDTO.getPassword().trim()));
    player.setKingdom(null);
    player.setAvatar("");
    player.setPoints(0);
    return player;
  }
}
