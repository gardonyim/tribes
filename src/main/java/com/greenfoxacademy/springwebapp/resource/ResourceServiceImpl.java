package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import java.util.List;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.utilities.EpochConverter;
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
        EpochConverter.convertToEpochMilli(resource.getUpdatedAt())
    );
  }

  @Override
  public List<ResourceDTO> convertToResourceDtoList(List<Resource> resourceList) {
    return resourceList.stream()
        .map(this::convertToResourceDTO)
        .collect(Collectors.toList());
  }

}