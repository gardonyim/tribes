package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.PlayerListDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;

import java.util.Optional;

public interface PlayerService {

  RegistrationResDTO savePlayer(RegistrationReqDTO reqDTO);

  Optional<Player> findFirstByUsername(String username);

  PlayerListDTO findNearbyPlayers(Player authPlayer, Integer distance);

}
