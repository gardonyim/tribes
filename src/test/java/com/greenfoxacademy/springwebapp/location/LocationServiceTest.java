package com.greenfoxacademy.springwebapp.location;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.location.models.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

  @Mock
  LocationRepository locationRepository;

  @Spy
  @InjectMocks
  LocationServiceImpl locationService;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void when_createLocation_should_returnUnoccupiedLocation() {
    List<Location> occupiedLocations = Arrays.asList(
        new Location(0, 0),
        new Location(0, 1),
        new Location(1, 0)
    );
    when(locationRepository.findAll()).thenReturn(occupiedLocations);
    when(locationRepository.save(any(Location.class))).then(returnsFirstArg());

    for (int i = 0; i < 10; i++) {
      Location createdLocation = locationService.createLocation(2, 0);

      Assert.assertEquals(1, createdLocation.getxcoordinate());
      Assert.assertEquals(1, createdLocation.getycoordinate());
    }
  }

  @Test
  public void when_createLocation_should_returnValidLocation() {
    List<Location> occupiedLocations = new ArrayList<>();
    when(locationRepository.findAll()).thenReturn(occupiedLocations);
    when(locationRepository.save(any(Location.class))).then(returnsFirstArg());

    for (int i = 0; i < 10; i++) {
      Location createdLocation = locationService.createLocation(2, 0);

      Assert.assertTrue(createdLocation.getxcoordinate() >= 0
          && createdLocation.getxcoordinate() <= 1);
      Assert.assertTrue(createdLocation.getycoordinate() >= 0
          && createdLocation.getycoordinate() <= 1);
    }
  }

  @Test
  public void when_createLocationOnFullBoard_should_returnException() {
    List<Location> occupiedLocations = Arrays.asList(
        new Location(0, 0),
        new Location(0, 1),
        new Location(1, 0),
        new Location(1, 1)
    );
    when(locationRepository.findAll()).thenReturn(occupiedLocations);

    exceptionRule.expect(RequestCauseConflictException.class);
    exceptionRule.expectMessage("Too many items on the board, unable to place new kingdom");
    locationService.createLocation(2, 0);
  }

  @Test
  public void when_createLocation_should_returnLocationOnTheBoard() {
    List<Location> occupiedLocations = new ArrayList<>();
    when(locationRepository.findAll()).thenReturn(occupiedLocations);
    when(locationRepository.save(any(Location.class))).then(returnsFirstArg());
    ArgumentCaptor<Integer> boardSize = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> offset = ArgumentCaptor.forClass(Integer.class);

    Location createdLocation = locationService.createLocation();

    Mockito.verify(locationService).createLocation(boardSize.capture(), offset.capture());
    Assert.assertEquals(201, (int) boardSize.getValue());
    Assert.assertEquals(-100, (int) offset.getValue());
  }

  @Test
  public void when_findShortestPath_should_returnListOfWayPoints() {
    Location start = new Location(0,2);
    Location destination = new Location(2,0);
    List<Location> obstacles = Arrays.asList(new Location(0,1), new Location(1, 0), new Location(2,2));
    when(locationRepository.findAll()).thenReturn(obstacles);

    List<Location> path = locationService.findShortestPath(start, destination, 3, 0);

    Assert.assertEquals(5, path.size());
    Assert.assertEquals(start, path.get(0));
    Assert.assertEquals(new Location(1,2), path.get(1));
    Assert.assertEquals(new Location(1,1), path.get(2));
    Assert.assertEquals(new Location(2,1), path.get(3));
    Assert.assertEquals(destination, path.get(4));
  }

  @Test
  public void when_findShortestPathNotExist_should_throwException() {
    List<Location> obstacles = Arrays.asList(new Location(0,1), new Location(1, 0),
        new Location(2,2), new Location(2,1));
    when(locationRepository.findAll()).thenReturn(obstacles);
    Location start = new Location(0,2);
    Location destination = new Location(2,0);

    exceptionRule.expect(RuntimeException.class);
    exceptionRule.expectMessage("Not possible to get to destination");
    List<Location> path = locationService.findShortestPath(start, destination, 3, 0);
  }

  @Test
  public void when_findShortestPathDistantLocations_should_returnPathWithCorrectNumberOfSteps() {
    when(locationRepository.findAll()).thenReturn(new ArrayList<Location>());
    Location start = new Location(0, 0);
    Location destination = new Location(50, 50);

    List<Location> path = locationService.findShortestPath(start, destination, 201, -100);

    Assert.assertEquals(101, path.size());
  }

}
