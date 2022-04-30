package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Location;

public interface LocationService {

  Location createLocation();

  Location createLocation(int boardSiza, int offset);

}
