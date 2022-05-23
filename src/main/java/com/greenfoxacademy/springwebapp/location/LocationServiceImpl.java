package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.location.models.Location;
import java.util.List;
import java.util.Random;

import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

  private LocationRepository locationRepository;

  @Autowired
  public LocationServiceImpl(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  @Override
  public Location createLocation() {
    int boardSize = 201;
    int offset = -100;
    return createLocation(boardSize, offset);
  }

  @Override
  public Location createLocation(int boardSize, int offset) {
    List<Location> locations = locationRepository.findAll();
    Location newLocation;
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      int x = random.nextInt(boardSize) + offset;
      int y = random.nextInt(boardSize) + offset;
      if (locations.stream()
          .noneMatch(location -> location.getxcoordinate() == x && location.getycoordinate() == y)) {
        try {
          newLocation = locationRepository.save(new Location(x, y));
        } catch (DataIntegrityViolationException e) {
          newLocation = createLocation();
        }
        return newLocation;
      }
    }
    throw new RequestCauseConflictException("Too many items on the board, unable to place new kingdom");
  }

  @Override
  public LocationDTO convertToLocationDTO(Location location) {
    return new LocationDTO(
        location.getxcoordinate(),
        location.getycoordinate()
    );
  }

}
