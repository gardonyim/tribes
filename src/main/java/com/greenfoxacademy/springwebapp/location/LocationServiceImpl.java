package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.location.models.Location;

import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

@Service
public class LocationServiceImpl implements LocationService {

  private LocationRepository locationRepository;
  private int boardSize = 201;
  private int offset = -100;

  @Autowired
  public LocationServiceImpl(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
  }

  @Override
  public Location createLocation() {
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

  @Override
  public List<Location> findShortestPath(Location startLocation, Location destinationLocation) {
    return findShortestPath(startLocation, destinationLocation, boardSize, offset);
  }

  @Override
  public List<Location> findShortestPath(Location start, Location destination, int boardSize, int offset) {
    List<Location> obstacles = locationRepository.findAll();
    Map<Location, Location> visited = new HashMap<>();
    visited.put(start, null);
    PriorityQueue<Location> queue =
            new PriorityQueue<>((l1, l2) -> getDistance(l1, destination).compareTo(getDistance(l2, destination)));
    queue.add(start);
    Location p =
        breadthFirstSearchForPath(destination, boardSize, offset, obstacles, visited, queue);
    return constructPath(offset, visited, p);
  }

  private Integer getDistance(Location start, Location end) {
    return Math.abs(end.getxcoordinate() - start.getxcoordinate())
            + Math.abs(end.getycoordinate() - start.getycoordinate());
  }

  private Location breadthFirstSearchForPath(Location destinationLocation, int boardSize, int offset,
                                             List<Location> obstacles, Map<Location, Location> visited,
                                             PriorityQueue<Location> queue) {
    Location p;
    boolean arrived = false;
    while ((p = queue.poll()) != null) {
      if (p.equals(destinationLocation)) {
        arrived = true;
        break;
      }
      visit(obstacles, visited, queue, p.getxcoordinate() + 1, p.getycoordinate(), p, boardSize, offset);
      visit(obstacles, visited, queue, p.getxcoordinate(), p.getycoordinate() + 1, p, boardSize, offset);
      visit(obstacles, visited, queue, p.getxcoordinate() - 1, p.getycoordinate(), p, boardSize, offset);
      visit(obstacles, visited, queue, p.getxcoordinate(), p.getycoordinate() - 1, p, boardSize, offset);
    }
    if (!arrived) {
      throw new RuntimeException("Not possible to get to destination");
    }
    return p;
  }

  private LinkedList<Location> constructPath(int offset, Map<Location, Location> visited, Location p) {
    LinkedList<Location> path = new LinkedList<>();
    while (visited.get(p) != null) {
      path.addFirst(p);
      p = visited.get(p);
    }
    path.addFirst(p);
    return path;
  }

  private void visit(List<Location> obstacles, Map<Location, Location> visited, PriorityQueue<Location> queue,
                     int x, int y, Location parent, int boardSize, int offset) {
    if (x < offset || x >= boardSize || y < offset || y >= boardSize
        || obstacles.stream().anyMatch(l -> l.getxcoordinate() == x && l.getycoordinate() == y)) {
      return;
    }
    Location p = new Location(x, y);
    if (!visited.containsKey(p)) {
      visited.put(p, parent);
      queue.add(p);
    }
  }

}
