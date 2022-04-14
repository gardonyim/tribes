package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImp implements PlayerService {

  private PlayerRepository playerRepository;

  @Autowired
  public PlayerServiceImp(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public Player savePlayer() {
    return playerRepository.save(new Player("foo", "", 0, "", 0));
  }
}
