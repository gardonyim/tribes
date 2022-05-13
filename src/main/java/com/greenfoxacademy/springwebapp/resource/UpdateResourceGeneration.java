package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;

public class UpdateResourceGeneration {

  private ResourceService resourceService;
  private Resource resource;
  private int generationChange;

  public UpdateResourceGeneration(
      ResourceService resourceService,
      Resource resource, int generationChange) {
    this.resourceService = resourceService;
    this.resource = resource;
    this.generationChange = generationChange;
  }

  public void updateResourceGeneration() {
    resource = resourceService.updateResource(resource);
    resource = setGenerationValue(resource, generationChange);
    resourceService.save(resource);
  }

  private Resource setGenerationValue(Resource resource, int generationChange) {
    resource.setGeneration(resource.getGeneration() + generationChange);
    return resource;
  }
}
