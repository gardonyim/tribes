package com.greenfoxacademy.springwebapp.login.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResponseDTO {

  @JsonProperty("status")
  private String status;
  @JsonProperty("token")
  private String token;

  public StatusResponseDTO(String status, String token) {
    this.status = status;
    this.token = token;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
