package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import org.springframework.data.repository.CrudRepository;

public interface KingdomRepository extends CrudRepository<Kingdom, Integer> {

}
