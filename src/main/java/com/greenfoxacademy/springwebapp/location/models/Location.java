package com.greenfoxacademy.springwebapp.location.models;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "locations")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "x_coordinate")
  private int xcoordinate;
  @Column(name = "y_coordinate")
  private int ycoordinate;
  @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
  private Kingdom kingdom;

  public Location(int xcoordinate, int ycoordinate) {
    this.xcoordinate = xcoordinate;
    this.ycoordinate = ycoordinate;
  }

  public Location() {
  }

  public int getxXcoordinate() {
    return xcoordinate;
  }

  public int getyYcoordinate() {
    return ycoordinate;
  }
}
