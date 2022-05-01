package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import java.util.List;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
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

  @Override
  public Iterable<Resource> saveAll(List<Resource> resources) {
    return resourceRepository.saveAll(resources);
  }

  @Override
  public ResourceDTO convertToResourceDTO(Resource resource) {
    if (resource == null) {
      return null;
    }
    return new ResourceDTO(
        resource.getResourceType().getDescription(),
        resource.getAmount(),
        resource.getGeneration(),
        TimeService.toEpochSecond(resource.getUpdatedAt())
    );
  }

  @Override
  public ResourcesResDTO convertToResourcesResDto(List<Resource> resourceList) {
    return new ResourcesResDTO(resourceList.stream()
        .map(this::convertToResourceDTO)
        .collect(Collectors.toList()));

  @Override
  public Resource getResourceByKingdomAndType(Kingdom kingdom, ResourceType type) {
    return resourceRepository.findFirstByKingdomAndResourceType(kingdom, type).get();
  }

  @Override
  public Resource pay(Kingdom kingdom, int price) {
    Resource gold = getResourceByKingdomAndType(kingdom, ResourceType.GOLD);
    gold.setAmount(gold.getAmount() - price);
    return save(gold);
  }

}