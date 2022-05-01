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
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceTest {

  @Mock
  ResourceRepository resourceRepository;

  @InjectMocks
  ResourceServiceImpl resourceService;

  @Test
  public void when_convertResource_should_returnResourceDTO () {

    Resource resource = new Resource();
    resource.setResourceType(ResourceType.FOOD);
    resource.setAmount(100);
    resource.setGeneration(10);
    resource.setUpdatedAt(LocalDateTime.now());

    //TODO: powerMock for static methodes
    when(TimeService.toEpochSecond(any(LocalDateTime.class))).thenReturn(5000L);

    ResourceDTO expected = new ResourceDTO(
        resource.getResourceType().getDescription(),
        resource.getAmount(),
        resource.getGeneration(),
        TimeService.toEpochSecond(resource.getUpdatedAt())
    );

    ResourceDTO actual = resourceService.convertToResourceDTO(resource);

    Assert.assertEquals(expected.getType(), actual.getType());
    Assert.assertEquals(expected.getAmount(), actual.getAmount());
    Assert.assertEquals(expected.getGeneration(), actual.getGeneration());
    Assert.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
  }

  @Test
  public void when_convertResourceList_should_returnResourceDTOList () {

    Resource resource1 = new Resource();
    resource1.setResourceType(ResourceType.FOOD);
    resource1.setAmount(100);
    resource1.setGeneration(10);
    resource1.setUpdatedAt(LocalDateTime.now());
    Resource resource2 = new Resource();
    resource2.setResourceType(ResourceType.GOLD);
    resource2.setAmount(1000);
    resource2.setGeneration(100);
    resource2.setUpdatedAt(LocalDateTime.now());

    ResourceDTO resourceDTO1 = new ResourceDTO(
        resource1.getResourceType().getDescription(),
        resource1.getAmount(),
        resource1.getGeneration(),
        TimeService.toEpochSecond(resource1.getUpdatedAt())
    );
    ResourceDTO resourceDTO2 = new ResourceDTO(
        resource2.getResourceType().getDescription(),
        resource2.getAmount(),
        resource2.getGeneration(),
        TimeService.toEpochSecond(resource2.getUpdatedAt())
    );

    //TODO: powerMock for static methodes
    when(TimeService.toEpochSecond(any(LocalDateTime.class))).thenReturn(5000L);

    List<ResourceDTO> expexted = new ArrayList<>(Arrays.asList(resourceDTO1, resourceDTO2));

    List<ResourceDTO> actual =
        resourceService.convertToResourceDtoList(new ArrayList<>(Arrays.asList(resource1, resource2)));

    Assert.assertEquals(expexted.size(), actual.size());
    Assert.assertEquals(expexted.get(0).getType(), actual.get(0).getType());
    Assert.assertEquals(expexted.get(0).getAmount(), actual.get(0).getAmount());
    Assert.assertEquals(expexted.get(0).getGeneration(), actual.get(0).getGeneration());
    Assert.assertEquals(expexted.get(0).getUpdatedAt(), actual.get(0).getUpdatedAt());
    // TODO: érdemes equalst írni, de azt is előtte tesztelni kell????
  }

}
