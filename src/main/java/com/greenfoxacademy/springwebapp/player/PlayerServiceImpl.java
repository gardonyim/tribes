package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        throw new RequestParameterMissingException("Username and password are required.");
      } else {
        throw new RequestParameterMissingException("Password is required.");
      }
    }
    if (reqDTO.getUsername() == null || reqDTO.getUsername().trim().isEmpty()) {
      throw new RequestParameterMissingException("Username is required.");
    }
    int minPasswordLength = 8;
    if (reqDTO.getPassword().trim().length() < minPasswordLength) {
      throw new RequestNotAcceptableException("Password must be at least 8 characters.");
    }
    if (playerRepository.findFirstByUsername(reqDTO.getUsername()).isPresent()) {
      throw new RequestCauseConflictException("Username is already taken.");
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

  @Override
  public Optional<Player> findByName(String username) {
    return playerRepository.findByUsername(username);
  }


}
