package com.greenfoxacademy.springwebapp.troop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TroopsDTO {

  @JsonProperty("troops")
  private List<TroopDTO> troops;

  public TroopsDTO(List<TroopDTO> troops) {
    this.troops = troops;
  }

  public List<TroopDTO> getTroops() {
    return troops;
  }

  public void setTroops(List<TroopDTO> troops) {
    this.troops = troops;
  }
}
