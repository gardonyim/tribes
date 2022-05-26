package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.PlayerListDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.greenfoxacademy.springwebapp.utilities.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;
  private KingdomService kingdomService;
  private PasswordEncoder pwEnc;
  private EmailService emailService;

  public static final int DEFAULT_DISTANCE = 10;

  @Autowired
  public void setPlayerRepository(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Autowired
  public void setKingdomService(KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

  @Autowired
  public void setPwEnc(PasswordEncoder pwEnc) {
    this.pwEnc = pwEnc;
  }

  @Autowired
  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }

  @Override
  public RegistrationResDTO savePlayer(RegistrationReqDTO reqDTO) {
    validateRegistration(reqDTO);
    Player player = convert(reqDTO);
    player.setEnabled(false);
    player.setActivation(generateKey());
    player = playerRepository.save(player);
    emailService.sendMessage(player.getEmail(), player.getActivation());
    player.setKingdom(kingdomService.save(reqDTO.getKingdomname(), player));
    return new RegistrationResDTO(player);
  }

  @Override
  public String userActivation(String code) {
    Player player = playerRepository.findFirstByActivation(code).orElseThrow(ForbiddenActionException::new);
    player.setActivation("");
    player.setEnabled(true);
    playerRepository.save(player);
    return "successful activation";
  }

  public String generateKey() {
    String key = "";
    Random random = new Random();
    char[] word = new char[16];
    for (int j = 0; j < word.length; j++) {
      word[j] = (char) ('a' + random.nextInt(26));
    }
    return new String(word);
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
    player.setEmail(reqDTO.getEmail());
    return player;
  }

  @Override
  public Optional<Player> findFirstByUsername(String username) {
    Optional<Player> player = playerRepository.findFirstByUsername(username);
    return player;
  }

  @Override
  public PlayerListDTO findNearbyPlayers(Player authPlayer, Integer distance) {
    distance = distance == null ? DEFAULT_DISTANCE : distance;
    int currentX = authPlayer.getKingdom().getLocation().getxcoordinate();
    int currentY = authPlayer.getKingdom().getLocation().getycoordinate();
    List<RegistrationResDTO> nearbyPlayerDTOs = playerRepository.findAllNearBy(
        currentX - distance, currentX + distance,
        currentY - distance, currentY + distance).stream()
        .filter(p -> p.getId() != authPlayer.getId())
        .map(RegistrationResDTO::new)
        .collect(Collectors.toList());
    return new PlayerListDTO(nearbyPlayerDTOs);
  }

}