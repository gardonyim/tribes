package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;

import java.util.List;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;

public interface ResourceService {
  Resource save(Resource resource);

  Iterable<Resource> saveAll(List<Resource> resources);

  ResourceDTO convertToResourceDTO(Resource resource);

  List<ResourceDTO> convertToResourceDTOs(List<Resource> resources);

  ResourcesResDTO convertToResourcesResDto(List<Resource> resourceList);

  Resource getResourceByKingdomAndType(Kingdom kingdom, ResourceType type);

  Resource pay(Kingdom kingdom, int amount);

  boolean hasEnoughGold(Kingdom kingdom, int amount);

  Kingdom updateResources(Kingdom kingdom);

  Resource updateResource(Resource resource);

  int calculateAvailableResource(Resource resource);

}