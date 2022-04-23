package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  private final PlayerRepository playerRepository;

  @Autowired
  public LoginServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public Player authenticate(String username, String password) {
    return playerRepository.findPlayerByUsernameAndPassword(username, password);
  }
}
