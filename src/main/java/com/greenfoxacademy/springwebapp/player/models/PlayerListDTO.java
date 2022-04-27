package com.greenfoxacademy.springwebapp.player.models;

import java.util.List;

public class PlayerListDTO {

  private List<RegistrationResDTO> players;

  public PlayerListDTO(List<RegistrationResDTO> players) {
    this.players = players;
  }

  public List<RegistrationResDTO> getPlayers() {
    return players;
  }

}
