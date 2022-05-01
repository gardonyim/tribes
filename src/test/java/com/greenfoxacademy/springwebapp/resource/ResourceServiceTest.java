package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.util.Optional;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

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

    Assert.assertTrue(expected.equals(actual));
  }

  @Test
  public void when_convertResourceList_should_returnResourceDtoList() {
    Resource resource1 = new Resource(ResourceType.FOOD, 100, 10, null, null);
    Resource resource2 = new Resource(ResourceType.GOLD, 1000, 100, null, null);
    ResourceDTO resourceDTO1 = new ResourceDTO(
        resource1.getResourceType().getDescription(), resource1.getAmount(),
        resource1.getGeneration(), 5000L);
    ResourceDTO resourceDTO2 = new ResourceDTO(
        resource2.getResourceType().getDescription(), resource2.getAmount(),
        resource2.getGeneration(), 5000L);
    ResourcesResDTO expected =
        new ResourcesResDTO(new ArrayList<>(Arrays.asList(resourceDTO1, resourceDTO2)));
    ResourcesResDTO actual;
    try (
        MockedStatic<TimeService> timeServiceMockedStatic = Mockito.mockStatic(TimeService.class)) {
      timeServiceMockedStatic.when(() -> TimeService.toEpochSecond(any())).thenReturn(5000L);
      actual = resourceService.convertToResourcesResDto(
          new ArrayList<>(Arrays.asList(resource1, resource2)));
    }

    Assert.assertEquals(expected.getResources().size(), actual.getResources().size());
    Assert.assertTrue(expected.getResources().get(0).equals(actual.getResources().get(0)));
    Assert.assertTrue(expected.getResources().get(1).equals(actual.getResources().get(1)));
  }

  @Test
  public void when_saveResource_should_returnSavedResource() {
    when(resourceRepository.save(any(Resource.class))).then(returnsFirstArg());
    Resource expected = new Resource();

    Resource actual = resourceService.save(expected);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void when_getResourceByKingdomAndType_should_returnValidResource() {
    when(resourceRepository.findFirstByKingdomAndResourceType(any(), any()))
        .thenReturn(Optional.of(new Resource()));

    Assert.assertTrue(
        resourceService.getResourceByKingdomAndType(new Kingdom(), ResourceType.GOLD) != null);
  }

  @Test
  public void when_pay_should_setGoldToValidAmount() {
    Resource gold = new Resource(ResourceType.GOLD, 1000);
    when(resourceRepository.findFirstByKingdomAndResourceType(any(), any())).thenReturn(Optional.of(gold));
    when(resourceRepository.save(any())).then(returnsFirstArg());

    Resource actual = resourceService.pay(new Kingdom(), 100);

    Assert.assertEquals(ResourceType.GOLD, actual.getResourceType());
    Assert.assertEquals(900, actual.getAmount());
  }

}
