package com.greenfoxacademy.springwebapp.utilities;

import com.greenfoxacademy.springwebapp.player.models.Player;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtUtils {

  @Value("${security.jwt-key:}")
  private String jwtKey;

  public String generateJwtString(Player player) {
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

}
