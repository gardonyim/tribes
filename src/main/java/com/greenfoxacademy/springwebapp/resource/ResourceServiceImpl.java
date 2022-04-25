package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceServiceImpl implements ResourceService {

  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceServiceImpl(ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

  @Override
  public Resource save(Resource resource) {
    return resourceRepository.save(resource);
  }
}
