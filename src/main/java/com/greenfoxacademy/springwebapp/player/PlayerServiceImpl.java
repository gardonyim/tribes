package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public Player savePlayer(RegistrationReqDTO reqDTO) {
    return playerRepository.save(new Player(reqDTO.getUsername(), "", null, "", 0));
  }
}
