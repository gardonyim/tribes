package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.dtos.StatusResponseDTO;
import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.models.Player;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

  @Value("${jwt.key}")
  private String jwtKey;

  @Autowired
  public LoginServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public Optional<Player> authenticate(String username, String password) {
    return playerRepository.findPlayerByUsernameAndPassword(username, password);
  }

  @Override
  public ResponseEntity<Object> authenticateWithLoginDTO(LoginDTO loginDTO) {
    String username = loginDTO.getUsername();
    String password = loginDTO.getPassword();

    if (username == null && password == null) {
      return ResponseEntity.badRequest().body(new ErrorDTO("All fields are required."));
    } else if (username == null) {
      return ResponseEntity.badRequest().body(new ErrorDTO("Username is required."));
    } else if (password == null) {
      return ResponseEntity.badRequest().body(new ErrorDTO("Password is required."));
    }

    Optional<Player> player = authenticate(username, password);
    if (!player.isPresent()) {
      return ResponseEntity.status(401).body(new ErrorDTO("Username or password is incorrect."));
    } else {
      return ResponseEntity.ok(new StatusResponseDTO("ok", generateJwtString(player.get())));
    }
  }

  private String generateJwtString(Player player) {
    SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
            .setIssuer("CH4-2")
            .setSubject("CH4")
            .claim("username", player.getUsername())
            .claim("kingdomId", player.getKingdom().getId())
            .claim("kingdomName", player.getKingdom().getName())
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(30 * 60, ChronoUnit.SECONDS)))
            .signWith(key)
            .compact();
  }
}
