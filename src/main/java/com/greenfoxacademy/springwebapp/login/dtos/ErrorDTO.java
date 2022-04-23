package com.greenfoxacademy.springwebapp.login.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDTO {

  @JsonProperty("error")
  private String error;

  public ErrorDTO(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
