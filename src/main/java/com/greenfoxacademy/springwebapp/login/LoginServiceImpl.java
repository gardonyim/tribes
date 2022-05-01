package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.dtos.StatusResponseDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.models.Player;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

  private final PlayerRepository playerRepository;

  private final PasswordEncoder passwordEncoder;

  @Value("${security.jwt-key}")
  private String jwtKey;

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
  public StatusResponseDTO authenticateWithLoginDTO(LoginDTO loginDTO)
          throws InputMissingException, InputWrongException {
    String username = loginDTO.getUsername();
    String password = loginDTO.getPassword();

    checkLoginDTO(username, password);

    Optional<Player> player = findPlayerByName(username);
    if (player.isPresent()) {
      if (passwordEncoder.matches(password, player.get().getPassword())) {
        return new StatusResponseDTO("ok", generateJwtString(player.get()));
      }
    }
    throw new InputWrongException("Username or password is incorrect.");
  }

  private String generateJwtString(Player player) {
    SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
            .setSubject("CH4")
            .claim("username", player.getUsername())
            .claim("kingdomId", player.getKingdom().getId())
            .claim("kingdomName", player.getKingdom().getName())
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(30 * 60, ChronoUnit.SECONDS)))
            .signWith(key)
            .compact();
  }

  private void checkLoginDTO(String username, String password)
          throws InputMissingException {
    boolean isUsernameCorrect = notBlank(username);
    boolean isPasswordCorrect = notBlank(password);
    if (!isUsernameCorrect && !isPasswordCorrect) {
      throw new InputMissingException("All fields are required.");
    } else if (!isUsernameCorrect) {
      throw new InputMissingException("Username is required.");
    } else if (!isPasswordCorrect) {
      throw new InputMissingException("Password is required.");
    }
  }

  private boolean notBlank(String text) {
    return text != null && !text.trim().isEmpty();
  }
}