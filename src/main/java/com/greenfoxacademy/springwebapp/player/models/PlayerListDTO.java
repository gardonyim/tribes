package com.greenfoxacademy.springwebapp.player.models;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerListDTO {

  private List<RegistrationResDTO> players;

  public PlayerListDTO(List<RegistrationResDTO> players) {
    this.players = players;
  }

  public List<RegistrationResDTO> getPlayers() {
    return players;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlayerListDTO that = (PlayerListDTO) o;

    return players != null ? players.stream().map(r -> r.getId()).collect(Collectors.toList())
        .equals(that.players.stream().map(r -> r.getId()).collect(Collectors.toList()))
        : that.players == null;
  }

  //  @Override
  //  public int hashCode() {
  //    return players != null ? players.hashCode() : 0;
  //  }
}
