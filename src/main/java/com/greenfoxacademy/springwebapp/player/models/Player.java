package com.greenfoxacademy.springwebapp.player.models;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Collection;

@Entity(name = "players")
public class Player implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(length = 50)
  private String username;
  private String password;
  @OneToOne(mappedBy = "player")
  private Kingdom kingdom;
  private String email;
  private String avatar;
  private Integer points;
  private String activation;
  private Boolean enabled;

  public Player(int id, String username, String password,
                Kingdom kingdom, String avatar, Integer points) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.kingdom = kingdom;
    this.avatar = avatar;
    this.points = points;
  }

  public Player(String username, String password, Kingdom kingdom, String avatar, Integer points) {
    this.username = username;
    this.password = password;
    this.kingdom = kingdom;
    this.avatar = avatar;
    this.points = points;
  }

  public Player() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public String getActivation() {
    return activation;
  }

  public void setActivation(String activation) {
    this.activation = activation;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getEnabled() {
    return enabled;
  }

}

