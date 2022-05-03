package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    ResourceDTO resourceDTO1 = new ResourceDTO(resource1.getResourceType().getDescription(),
        resource1.getAmount(), resource1.getGeneration(), 5000L);
    ResourceDTO resourceDTO2 = new ResourceDTO(resource2.getResourceType().getDescription(),
        resource2.getAmount(), resource2.getGeneration(), 5000L);
    ResourcesResDTO expected =
        new ResourcesResDTO(new ArrayList<>(Arrays.asList(resourceDTO1, resourceDTO2)));
    ResourcesResDTO actual;
    try (
        MockedStatic<TimeService> timeServiceMockedStatic = Mockito.mockStatic(TimeService.class)) {
      timeServiceMockedStatic.when(() -> TimeService.toEpochSecond(any())).thenReturn(5000L);
      actual = resourceService.convertToResourcesResDto(new ArrayList<>(Arrays.asList(resource1, resource2)));
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
  public void when_pay_should_setGoldToValidAmountAndUpdatedAtToCurrentTime() {
    Kingdom kingdom = new Kingdom();
    Resource gold = new Resource(ResourceType.GOLD, 1000, 2,
        LocalDateTime.now(ZoneOffset.UTC).minusSeconds(101), kingdom);
    when(resourceRepository.findFirstByKingdomAndResourceType(any(), any())).thenReturn(Optional.of(gold));
    when(resourceRepository.save(any())).then(returnsFirstArg());

    Resource actual = resourceService.pay(kingdom, 100);

    Assert.assertEquals(ResourceType.GOLD, actual.getResourceType());
    Assert.assertEquals(1100, actual.getAmount());
  }

  @Test
  public void when_updateResources_should_returnKingdomWithUpdatedResources() {
    LocalDateTime previousUpdate = LocalDateTime.now(ZoneOffset.UTC).minusSeconds(101);
    Kingdom kingdom = new Kingdom();
    List<Resource> resources = new ArrayList<>();
    resources.add(new Resource(ResourceType.GOLD, 1000, 1, previousUpdate, kingdom));
    resources.add(new Resource(ResourceType.FOOD, 1000, 2, previousUpdate, kingdom));
    kingdom.setResources(resources);
    when(resourceRepository.save(any())).then(returnsFirstArg());

    Kingdom actual = resourceService.updateResources(kingdom);

    Assert.assertEquals(1100, actual.getResources().get(0).getAmount());
    Assert.assertEquals(LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS),
        actual.getResources().get(0).getUpdatedAt());
    Assert.assertEquals(1200, actual.getResources().get(1).getAmount());
    Assert.assertEquals(LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS),
        actual.getResources().get(1).getUpdatedAt());
  }

  @Test
  public void when_updateResource_should_returnResourceWithUpdatedAmountAndUpdateTime() {
    Kingdom kingdom = new Kingdom();
    Resource resource = new Resource(ResourceType.FOOD, 1000, 5,
        LocalDateTime.now(ZoneOffset.UTC).minusSeconds(101), kingdom);
    when(resourceRepository.save(any())).then(returnsFirstArg());

    Resource actual = resourceService.updateResource(resource);

    Assert.assertEquals(ResourceType.FOOD, actual.getResourceType());
    Assert.assertEquals(1500, actual.getAmount());
    Assert.assertEquals(5, actual.getGeneration());
    Assert.assertEquals(LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS), actual.getUpdatedAt());
    Assert.assertEquals(kingdom, actual.getKingdom());
  }

  @Test
  public void when_calculateAvailableResource_should_returnValidAmount() {
    Kingdom kingdom = new Kingdom();
    Resource resource = new Resource(ResourceType.FOOD, 1000, 10,
        LocalDateTime.now(ZoneOffset.UTC).minusSeconds(101), kingdom);

    int actual = resourceService.calculateAvailableResource(resource);

    Assert.assertEquals(2000, actual);
  }

}
