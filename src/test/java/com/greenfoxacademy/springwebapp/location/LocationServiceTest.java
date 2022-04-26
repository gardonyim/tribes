package com.greenfoxacademy.springwebapp.location;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.springwebapp.location.models.Location;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class LocationServiceTest {

  @Mock
  LocationRepository locationRepository;

  @InjectMocks
  LocationServiceImpl locationService;

  @Test
  public void when_createLocation_should_returnUnoccupiedLocation() {
    Location existingLocation = new Location();
    when(locationRepository.findFirstByXcoordinateAndYcoordinate(anyInt(), anyInt()))
        .thenReturn(Optional.of(existingLocation))
        .thenReturn(Optional.of(existingLocation))
        .thenReturn(Optional.empty());
    when(locationRepository.save(any(Location.class))).then(returnsFirstArg());

    Location created = locationService.createLocation();

    Mockito.verify(locationRepository, times(3))
        .findFirstByXcoordinateAndYcoordinate(anyInt(), anyInt());
    Assert.assertNotNull(created);
    Assert.assertTrue(created.getxcoordinate() >= -100 && created.getxcoordinate() <= 100);
    Assert.assertTrue(created.getycoordinate() >= -100 && created.getycoordinate() <= 100);
  }

}
