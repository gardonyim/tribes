package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private PlayerService playerService;

  @Autowired
  public SecurityConfiguration(PlayerService playerService) {
    this.playerService = playerService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .addFilterBefore(new JwtTokenValidatorFilter(playerService),
          BasicAuthenticationFilter.class)
      .authorizeRequests()
      .antMatchers("/h2-console").permitAll()
      .antMatchers("/login").permitAll()
      .antMatchers("/register").permitAll()
      .antMatchers("/**").authenticated()
      .and()
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .exceptionHandling().authenticationEntryPoint(new AuthenticationExceptionHandler());
  }

}
