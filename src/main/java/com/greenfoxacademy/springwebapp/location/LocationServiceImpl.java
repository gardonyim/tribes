package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Location;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

  private LocationRepository locationRepository;
  private final int boardSize = 201;
  private final int offset = -100;

  @Autowired
  public LocationServiceImpl(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  @Override
  public Location createLocation() {
    Random random = new Random();
    int x;
    int y;
    do {
      x = random.nextInt(boardSize) + offset;
      y = random.nextInt(boardSize) + offset;
    } while (checkLocationOccupancy(x, y));
    return locationRepository.save(new Location(x, y));
  }

  private boolean checkLocationOccupancy(int x, int y) {
    return locationRepository.findAll().stream()
        .anyMatch(location -> (location.getxXcoordinate() == x && location.getyYcoordinate() == y));
  }
}
