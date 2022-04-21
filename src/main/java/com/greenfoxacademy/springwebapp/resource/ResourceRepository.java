package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {

}
