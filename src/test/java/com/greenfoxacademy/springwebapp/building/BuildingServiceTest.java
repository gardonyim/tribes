package com.greenfoxacademy.springwebapp.building;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildingServiceTest {

  @Mock
  private BuildingRepository buildingRepository;
  @Mock
  private ResourceServiceImpl resourceService;
  @Mock
  private GameObjectRuleHolder gameObjectRuleHolder;

  @InjectMocks
  private BuildingServiceImpl buildingService;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void when_saveBuilding_should_returnValidBuilding() {
    Building expected = new Building();
    when(buildingRepository.save(any())).then(returnsFirstArg());

    Building actual = buildingService.saveBuilding(expected);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void when_addBuildingWithoutType_should_throwException() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("");
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("Missing parameter(s): type!");
    buildingService.addBuilding(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_addBuildingWithInvalidType_should_throwException() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("whatever");
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Invalid building type");
    buildingService.addBuilding(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_addBuildingWithTownhallAsType_should_throwException() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("townhall");
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestCauseConflictException.class);
    exceptionRule.expectMessage("There must only be one Townhall in a kingdom");
    buildingService.addBuilding(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_addBuildingAndTownhallHasLevel0_should_throwException() {
    Kingdom kingdom = new Kingdom();
    Building townhall = new Building(BuildingType.TOWNHALL, 0, kingdom, null, null);
    when(buildingRepository.findFirstByBuildingTypeAndKingdom(any(), any()))
        .thenReturn(Optional.of(townhall));
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("mine");

    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Cannot build buildings with higher level than the Townhall");
    buildingService.addBuilding(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_addBuildingAndHasInsufficientGold_should_throwException() {
    Kingdom kingdom = new Kingdom();
    Resource gold = new Resource(ResourceType.GOLD, 50);
    Building townhall = new Building(BuildingType.TOWNHALL, 1, kingdom, null, null);
    when(buildingRepository.findFirstByBuildingTypeAndKingdom(any(), any()))
        .thenReturn(Optional.of(townhall));
    when(gameObjectRuleHolder.getBuildingCostMultiplier(anyString(), anyInt())).thenReturn(100);
    when(resourceService.getResourceByKingdomAndType(any(), any())).thenReturn(gold);
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("mine");

    exceptionRule.expect(RequestCauseConflictException.class);
    exceptionRule.expectMessage("Not enough resources");
    buildingService.addBuilding(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_addBuildingWithValidParameters_should_returnBuildingDTO() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("mine");
    Kingdom kingdom = new Kingdom();
    Resource gold = new Resource(ResourceType.GOLD, 100);
    Building townhall = new Building(BuildingType.TOWNHALL, 1, kingdom, null, null);
    when(buildingRepository.findFirstByBuildingTypeAndKingdom(any(), any()))
        .thenReturn(Optional.of(townhall));
    when(gameObjectRuleHolder.getBuildingCostMultiplier(anyString(), anyInt())).thenReturn(100);
    when(gameObjectRuleHolder.getHpMultiplier(anyString(), anyInt())).thenReturn(100);
    when(gameObjectRuleHolder.getBuildingTimeMultiplier(anyString(), anyInt())).thenReturn(60);
    when(resourceService.getResourceByKingdomAndType(any(), any())).thenReturn(gold);
    when(buildingRepository.save(any())).then(returnsFirstArg());
    BuildingDTO expected = new BuildingDTO(0, BuildingType.MINE, 1, 100,
        TimeService.toEpochSecond(TimeService.actualTime()),
        TimeService.toEpochSecond(TimeService.timeAtNSecondsLater(60)));

    BuildingDTO actual = buildingService.addBuilding(buildingTypeDTO, kingdom);

    Assert.assertEquals(expected, actual);
  }

}
