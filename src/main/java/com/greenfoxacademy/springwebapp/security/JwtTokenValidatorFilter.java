package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.models.PlayerTokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
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
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException, ResponseStatusException {
    String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
    if (jwt == null) {
      throw new BadCredentialsException("No authentication token is provided!");
    } else {
      jwt = jwt.substring(7).trim();
      try {
        SecretKey key = Keys.hmacShaKeyFor(
            SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .getBody();
        String username = String.valueOf(claims.get("username"));
        Integer kingdomId = claims.get("kingdomId", Integer.class);
        String kingdomName = String.valueOf(claims.get("kingdomName"));

        Authentication auth = new UsernamePasswordAuthenticationToken(
            new PlayerTokenDTO(-1, username, kingdomId, kingdomName, null, null),
            null,
            null);
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (Exception e) {
        throw new BadCredentialsException("Authentication token is invalid!");
      }
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String reqPath = request.getServletPath();
    return (reqPath.equals("/login") || reqPath.equals("/register"));
  }
}
