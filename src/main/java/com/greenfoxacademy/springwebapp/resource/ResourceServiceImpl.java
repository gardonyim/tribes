package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.utilities.SchedulingService;
import java.util.HashMap;
import java.util.List;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

  private static final Map<String, ResourceType> relatedResourceType = new HashMap<String, ResourceType>() {
    {
      put("farm", ResourceType.FOOD);
      put("mine", ResourceType.GOLD);
      put("troop", ResourceType.FOOD);
    }
  };

  private final ResourceRepository resourceRepository;
  private final GameObjectRuleHolder gameObjectRuleHolder;

  @Autowired
  public ResourceServiceImpl(ResourceRepository resourceRepository, GameObjectRuleHolder gameObjectRuleHolder) {
    this.resourceRepository = resourceRepository;
    this.gameObjectRuleHolder = gameObjectRuleHolder;
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
  public List<ResourceDTO> convertToResourceDTOs(List<Resource> resources) {
    return resources.stream().map(this::convertToResourceDTO).collect(Collectors.toList());
  }

  @Override
  public ResourcesResDTO convertToResourcesResDto(List<Resource> resourceList) {
    return new ResourcesResDTO(resourceList.stream()
        .map(this::convertToResourceDTO)
        .collect(Collectors.toList()));
  }

  @Override
  public Resource getResourceByKingdomAndType(Kingdom kingdom, ResourceType type) {
    return resourceRepository.findFirstByKingdomAndResourceType(kingdom, type)
        .orElseThrow(() -> new RequestedResourceNotFoundException("Kingdom or resource is not in database."));
  }

  @Override
  public Resource pay(Kingdom kingdom, int price) {
    Resource gold = getResourceByKingdomAndType(kingdom, ResourceType.GOLD);
    gold.setAmount(updateResource(gold).getAmount() - price);
    return gold;
  }

  @Override
  public Kingdom updateResources(Kingdom kingdom) {
    kingdom.getResources().forEach(this::updateResource);
    return kingdom;
  }

  @Override
  public Resource updateResource(Resource resource) {
    resource.setAmount(calculateAvailableResource(resource));
    resource.setUpdatedAt(TimeService.actualTime());
    return resource;
  }

  @Override
  public int calculateAvailableResource(Resource resource) {
    return resource.getAmount() + (int) (resource.getGeneration() / 60.0
        * TimeService.secondsElapsed(resource.getUpdatedAt(), TimeService.actualTime()));
  }

  @Override
  public boolean hasEnoughGold(Kingdom kingdom, int amount) {
    updateResources(kingdom);
    Resource gold = kingdom.getResources().stream()
            .filter(r -> r.getResourceType().equals(ResourceType.GOLD))
            .findFirst()
            .orElseThrow(NotEnoughResourceException::new);
    return gold.getAmount() >= amount;
  }

  @Override
  public void updateResourceGeneration(Kingdom kingdom, String type, int currentLevel, int reqLevel) {
    long delay = gameObjectRuleHolder.calcCreationTime(type, currentLevel, reqLevel);
    Resource resource = findResource(kingdom, relatedResourceType.get(type));
    int generationChange = gameObjectRuleHolder.calcGenerationChange(type, currentLevel, reqLevel);
    delayUpdate(delay, resource, generationChange);
  }

  @Override
  public void delayUpdate(long delay, Resource resource, int generationChange, int amountChange) {
    Runnable updateResGen = () -> {
      Resource updatedResource = updateResource(resource);
      updatedResource.setGeneration(updatedResource.getGeneration() + generationChange);
      updatedResource.setAmount(updatedResource.getAmount() + amountChange);
      save(updatedResource);
    };
    SchedulingService.scheduler(updateResGen, delay);
  }

  @Override
  public void delayUpdate(long delay, Resource resource, int generationChange) {
    delayUpdate(delay, resource, generationChange, 0);
  }

  @Override
  public Resource findResource(Kingdom kingdom, ResourceType type) {
    return kingdom.getResources().stream()
            .filter(r -> r.getResourceType() == type)
            .findFirst()
            .orElse(null);
  }

}