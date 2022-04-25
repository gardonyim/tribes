package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Location;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {

  List<Location> findAll();

}
