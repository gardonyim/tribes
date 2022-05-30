package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;

public interface LocationService {

  Location createLocation();

  Location createLocation(int boardSize, int offset);

  LocationDTO convertToLocationDTO(Location location);

}
