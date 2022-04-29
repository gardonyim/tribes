package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {

  Optional<Resource> findFirstByKingdomAndResourceType(Kingdom kingdom, ResourceType resourceType);

}