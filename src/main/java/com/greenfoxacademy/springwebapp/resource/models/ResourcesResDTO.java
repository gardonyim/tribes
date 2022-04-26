package com.greenfoxacademy.springwebapp.resource.models;

import java.util.List;

public class ResourcesResDTO {
  private List<ResourceDTO> resources;

  public ResourcesResDTO(List<ResourceDTO> resources) {
    this.resources = resources;
  }

  public List<ResourceDTO> getResources() {
    return resources;
  }

  public void setResources(List<ResourceDTO> resources) {
    this.resources = resources;
  }

}
