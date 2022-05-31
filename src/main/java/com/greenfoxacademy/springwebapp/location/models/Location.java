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

  public Location(int id, int xcoordinate, int ycoordinate) {
    this.id = id;
    this.xcoordinate = xcoordinate;
    this.ycoordinate = ycoordinate;
  }

  public Location(int xcoordinate, int ycoordinate) {
    this.xcoordinate = xcoordinate;
    this.ycoordinate = ycoordinate;
  }

  public Location() {
  }

  public int getxcoordinate() {
    return xcoordinate;
  }

  public int getycoordinate() {
    return ycoordinate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return xcoordinate == location.xcoordinate && ycoordinate == location.ycoordinate;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + xcoordinate;
    result = 31 * result + ycoordinate;
    result = 31 * result + (kingdom != null ? kingdom.hashCode() : 0);
    return result;
  }
}
