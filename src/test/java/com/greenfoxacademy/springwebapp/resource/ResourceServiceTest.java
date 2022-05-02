package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceTest {

  @Mock
  ResourceRepository resourceRepository;

  @InjectMocks
  ResourceServiceImpl resourceService;

  @Test
  public void when_convertResource_should_returnResourceDTO() {
    Resource resource = new Resource(ResourceType.FOOD, 100, 10, LocalDateTime.now(), null);
    ResourceDTO expected;
    ResourceDTO actual;

    try (MockedStatic<TimeService> timeServiceMockedStatic = Mockito.mockStatic(TimeService.class)) {
      timeServiceMockedStatic.when(() -> TimeService.toEpochSecond(any(LocalDateTime.class))).thenReturn(5000L);
      expected = new ResourceDTO(
          resource.getResourceType().getDescription(),
          resource.getAmount(),
          resource.getGeneration(),
          TimeService.toEpochSecond(resource.getUpdatedAt()));
      actual = resourceService.convertToResourceDTO(resource);
    }

    Assert.assertEquals(expected.getType(), actual.getType());
    Assert.assertEquals(expected.getAmount(), actual.getAmount());
    Assert.assertEquals(expected.getGeneration(), actual.getGeneration());
    Assert.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
  }

  @Test
  public void when_convertResourceList_should_returnResourceDtoList() {
    Resource resource1 = new Resource(ResourceType.FOOD, 100, 10, null, null);
    Resource resource2 = new Resource(ResourceType.GOLD, 1000, 100, null, null);
    ResourceDTO resourceDTO1 = new ResourceDTO(
        resource1.getResourceType().getDescription(), resource1.getAmount(), resource1.getGeneration(), 5000L);
    ResourceDTO resourceDTO2 = new ResourceDTO(
        resource2.getResourceType().getDescription(), resource2.getAmount(), resource2.getGeneration(), 5000L);
    List<ResourceDTO> expected = new ArrayList<>(Arrays.asList(resourceDTO1, resourceDTO2));
    List<ResourceDTO> actual;
    try (MockedStatic<TimeService> timeServiceMockedStatic = Mockito.mockStatic(TimeService.class)) {
      timeServiceMockedStatic.when(() -> TimeService.toEpochSecond(any())).thenReturn(5000L);
      actual = resourceService.convertToResourceDtoList(new ArrayList<>(Arrays.asList(resource1, resource2)));
    }

    Assert.assertEquals(expected.size(), actual.size());
    Assert.assertEquals(expected.get(0).getType(), actual.get(0).getType());
    Assert.assertEquals(expected.get(0).getAmount(), actual.get(0).getAmount());
    Assert.assertEquals(expected.get(0).getGeneration(), actual.get(0).getGeneration());
    Assert.assertEquals(expected.get(0).getUpdatedAt(), actual.get(0).getUpdatedAt());
  }

}
