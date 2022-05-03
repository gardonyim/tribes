package com.greenfoxacademy.springwebapp.kingdom.models;

import java.util.Objects;


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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KingdomResWrappedDTO that = (KingdomResWrappedDTO) o;
    return Objects.equals(kingdom, that.kingdom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kingdom);
  }
}
