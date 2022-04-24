package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.PlayerService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

  private PlayerService playerService;
  private AuthenticationExceptionHandler authenticationExceptionHandler;

  public JwtTokenValidatorFilter(PlayerService playerService, AuthenticationExceptionHandler authenticationExceptionHandler) {
    this.playerService = playerService;
    this.authenticationExceptionHandler = authenticationExceptionHandler;
  }

  @Override
  public Environment getEnvironment() {
    return super.getEnvironment();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException, ResponseStatusException {
    String jwt = request.getHeader("Authorization");
    if (jwt == null) {
      authenticationExceptionHandler.commence(
          request, response, new InsufficientAuthenticationException("No authentication token is provided!"));
    } else {
      jwt = jwt.substring(7).trim();
      try {
        SecretKey key = Keys.hmacShaKeyFor(
            getJWT_KEY().getBytes(StandardCharsets.UTF_8)
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(convert(jwt, key), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (Exception e) {
        authenticationExceptionHandler.commence(
            request, response, new InsufficientAuthenticationException("Authentication token is invalid!"));
      }
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String reqPath = request.getServletPath();
    return (reqPath.equals("/login") || reqPath.equals("/register"));
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
