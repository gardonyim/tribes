package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.PlayerService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.greenfoxacademy.springwebapp.security.SecurityConfiguration.PRIVATE_ENDPOINTS;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

  private PlayerService playerService;

  public JwtTokenValidatorFilter(PlayerService playerService) {
    this.playerService = playerService;
  }

  @Override
  public Environment getEnvironment() {
    return super.getEnvironment();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException, ResponseStatusException {
    if (shouldNotFilter(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    String jwt = request.getHeader("Authorization").substring(7).trim();

    SecretKey key = Keys.hmacShaKeyFor(
            getJWT_KEY().getBytes(StandardCharsets.UTF_8));

    Authentication auth = new UsernamePasswordAuthenticationToken(convert(jwt, key), null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    List<String> privateEndpoints = Arrays.stream(PRIVATE_ENDPOINTS)
            .map(endpoint -> endpoint.replaceAll("/\\*\\*", ""))
            .collect(Collectors.toList());
    return !privateEndpoints.stream().anyMatch(e -> path.startsWith(e));
  }

  private Player convert(String jwt, SecretKey key) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jwt)
        .getBody();
    String username = String.valueOf(claims.get("username"));
    return playerService.findFirstByUsername(username).get();
  }

  private String getJWT_KEY() {
    return getEnvironment().getProperty("JWT_KEY");
  }

}
