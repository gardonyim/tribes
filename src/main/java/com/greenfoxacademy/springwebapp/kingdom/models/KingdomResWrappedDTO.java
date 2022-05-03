package com.greenfoxacademy.springwebapp.kingdom.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KingdomResWrappedDTO {

  private KingdomBaseDTO kingdom;

  public KingdomResWrappedDTO() {
  }

  public KingdomResWrappedDTO(KingdomBaseDTO kingdom) {
    this.kingdom = kingdom;
  }

  public KingdomBaseDTO getKingdom() {
    return kingdom;
  }

  public void setKingdom(KingdomBaseDTO kingdom) {
    this.kingdom = kingdom;
  }
}
