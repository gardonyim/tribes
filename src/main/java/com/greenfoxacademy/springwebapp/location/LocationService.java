package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import java.util.List;

public interface LocationService {

  Location createLocation();

  Location createLocation(int boardSize, int offset);

  LocationDTO convertToLocationDTO(Location location);

  List<Location> findShortestPath(Location start, Location destination);

  List<Location> findShortestPath(Location start, Location destination, int boardSize, int offset);

}
