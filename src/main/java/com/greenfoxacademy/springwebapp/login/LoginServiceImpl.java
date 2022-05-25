package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.exceptions.RequestedForbiddenResourceException;
import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.dtos.StatusResponseDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.utilities.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

  private final PlayerRepository playerRepository;

  private final PasswordEncoder passwordEncoder;

  @Value("${security.jwt-key}")
  private String jwtKey;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  public LoginServiceImpl(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
    this.playerRepository = playerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<Player> findPlayerByName(String username) {
    return playerRepository.findFirstByUsername(username);
  }

  @Override
  public StatusResponseDTO authenticateWithLoginDTO(LoginDTO loginDTO) throws InputWrongException {
    String username = loginDTO.getUsername();
    String password = loginDTO.getPassword();

    Optional<Player> player = findPlayerByName(username);
    if (player.isPresent()) {
      if (passwordEncoder.matches(password, player.get().getPassword())) {
        if (player.get().getEnabled()) {
          return new StatusResponseDTO("ok", jwtUtils.generateJwtString(player.get()));
        } else {
          throw new RequestedForbiddenResourceException("Unactivated account");
        }
      }
    }
    throw new InputWrongException("Username or password is incorrect.");
  }

}