package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;

import java.util.List;

public interface ResourceService {
  Resource save(Resource resource);

  Iterable<Resource> saveAll(List<Resource> resources);

  ResourceDTO convertToResourceDTO(Resource resource);

  ResourcesResDTO convertToResourcesResDto(List<Resource> resourceList);

}