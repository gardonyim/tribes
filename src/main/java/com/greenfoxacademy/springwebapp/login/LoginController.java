package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.ErrorDTO;
import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.dtos.StatusResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Controller
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<Object> authentication(@RequestBody LoginDTO loginDTO) {
    String username = loginDTO.getUsername();
    String password = loginDTO.getPassword();

    if (username == null && password == null) {
      return ResponseEntity.badRequest().body(new ErrorDTO("All fields are required."));
    } else if (username == null) {
      return ResponseEntity.badRequest().body(new ErrorDTO("Username is required."));
    } else if (password == null) {
      return ResponseEntity.badRequest().body(new ErrorDTO("Password is required."));
    }

    Player player = loginService.authenticate(username, password);
    if (player == null) {
      return ResponseEntity.status(401).body(new ErrorDTO("Username or password is incorrect."));
    } else {
      return ResponseEntity.ok(new StatusResponseDTO("ok", generateJwtString(player)));
    }
  }

  private String generateJwtString(Player player) {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
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