package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface KingdomRepository extends CrudRepository<Kingdom, Integer> {

  public List<Kingdom> findAll();

  public Optional<Kingdom> findFirstByName(String name);

}
