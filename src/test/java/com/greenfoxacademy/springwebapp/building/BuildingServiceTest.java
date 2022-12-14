package com.greenfoxacademy.springwebapp.building;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingsDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildingServiceTest {

  @Mock
  private BuildingRepository buildingRepository;
  @Mock
  private ResourceServiceImpl resourceService;
  @Mock
  private GameObjectRuleHolder gameObjectRuleHolder;

  @Spy
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
  public void when_validateAddBuildingWithoutType_should_throwException() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("");
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("Missing parameter(s): type!");
    buildingService.addBuilding(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_validateAddBuildingWithInvalidType_should_throwException() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("whatever");
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Invalid building type");
    buildingService.validateAddBuildingRequest(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_validateAddBuildingWithTownhallAsType_should_throwException() {
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("townhall");
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestCauseConflictException.class);
    exceptionRule.expectMessage("There must only be one Townhall in a kingdom");
    buildingService.validateAddBuildingRequest(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_validateAddBuildingAndTownhallHasLevel0_should_throwException() {
    Kingdom kingdom = new Kingdom();
    Building townhall = new Building(BuildingType.TOWNHALL, 0, kingdom, null, null);
    when(buildingRepository.findFirstByBuildingTypeAndKingdom(any(), any()))
        .thenReturn(Optional.of(townhall));
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("mine");

    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Cannot build buildings with higher level than the Townhall");
    buildingService.validateAddBuildingRequest(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_validateAddBuildingAndHasInsufficientGold_should_throwException() {
    Kingdom kingdom = new Kingdom();
    Resource gold = new Resource(ResourceType.GOLD, 50);
    Building mine = new Building(BuildingType.MINE, 1, kingdom, null, null);
    when(buildingRepository.findFirstByBuildingTypeAndKingdom(any(), any()))
        .thenReturn(Optional.of(mine));
    when(gameObjectRuleHolder.calcCreationCost(anyString(), anyInt(), anyInt())).thenReturn(100);
    when(resourceService.hasEnoughGold(any(), anyInt())).thenReturn(false);
    BuildingTypeDTO buildingTypeDTO = new BuildingTypeDTO("mine");

    exceptionRule.expect(RequestCauseConflictException.class);
    exceptionRule.expectMessage("Not enough resources");
    buildingService.validateAddBuildingRequest(buildingTypeDTO, kingdom);
  }

  @Test
  public void when_constructBuilding_should_returnValidBuilding() {
    Kingdom kingdom = new Kingdom();
    LocalDateTime startedAt = LocalDateTime.now();
    LocalDateTime finishedAt = LocalDateTime.now().plusSeconds(60L);
    when(gameObjectRuleHolder.getHpMultiplier(anyString(), anyInt())).thenReturn(100);
    when(gameObjectRuleHolder.getBuildingTimeMultiplier(anyString(), anyInt())).thenReturn(60);
    try (MockedStatic<TimeService> timeServiceMockedStatic = Mockito.mockStatic(TimeService.class)) {
      timeServiceMockedStatic.when(() -> TimeService.actualTime()).thenReturn(startedAt);
      timeServiceMockedStatic.when(() -> TimeService.timeAtNSecondsLater(anyLong())).thenReturn(finishedAt);

      Building actual = buildingService.constructBuilding("mine", 1, kingdom);

      Assert.assertEquals(BuildingType.valueOf("mine".toUpperCase()), actual.getBuildingType());
      Assert.assertEquals(1, actual.getLevel());
      Assert.assertEquals(100, actual.getHp());
      Assert.assertEquals(kingdom, actual.getKingdom());
      Assert.assertEquals(startedAt, actual.getStartedAt());
      Assert.assertEquals(finishedAt, actual.getFinishedAt());
    }
  }

  @Test
  public void when_convertBuildingToBuildingDTO_should_returnValidBuildingDTO() {
    LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    Building building = new Building(BuildingType.FARM, 1, new Kingdom(), currentTime, currentTime);
    building.setId(999);
    long currentTimeInEpoch = currentTime.toEpochSecond(ZoneOffset.UTC);
    BuildingDTO expected = new BuildingDTO(999, BuildingType.FARM, 1, 100,
        currentTimeInEpoch, currentTimeInEpoch);

    BuildingDTO actual = buildingService.convertToDTO(building);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void when_addBuilding_should_returnValidBuildingDTO() {
    BuildingTypeDTO typeDTO = new BuildingTypeDTO("academy");
    Kingdom kingdom = new Kingdom();
    Building building = new Building();
    Mockito.doNothing().when(buildingService).validateAddBuildingRequest(typeDTO, kingdom);
    Mockito.doReturn(building).when(buildingService).constructBuilding(anyString(), anyInt(), any());
    when(gameObjectRuleHolder.calcCreationCost(anyString(), anyInt(), anyInt())).thenReturn(100);
    when(resourceService.pay(any(), anyInt())).thenReturn(null);
    when(buildingRepository.save(any())).then(returnsFirstArg());
    Mockito.doReturn(new BuildingDTO()).when(buildingService).convertToDTO(any());

    buildingService.addBuilding(typeDTO, kingdom);

    Mockito.verify(buildingService, times(1)).validateAddBuildingRequest(typeDTO, kingdom);
    Mockito.verify(buildingService, times(1)).constructBuilding("academy", 1, kingdom);
    Mockito.verify(gameObjectRuleHolder, times(1)).calcCreationCost("academy", 0, 1);
    Mockito.verify(resourceService, times(1)).pay(kingdom, 100);
    Mockito.verify(buildingRepository, times(1)).save(building);
    Mockito.verify(buildingService, times(1)).convertToDTO(building);
  }

  @Test
  public void when_requestKingdomWithoutBuildingId_should_returnValidBuildingDtoList() {
    LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    Kingdom kingdom = new Kingdom();
    Building farm = new Building(BuildingType.FARM, 1, kingdom, currentTime, currentTime);
    Building academy = new Building(BuildingType.ACADEMY, 2, kingdom, currentTime, currentTime);
    List<Building> buildings = Arrays.asList(farm,academy);
    kingdom.setBuildings(buildings);

    BuildingsDTO actual = buildingService.getBuildingDtoList(kingdom);

    assertThat(actual.getBuildings()).hasSize(2);
    assertThat(actual.getBuildings()).extracting(BuildingDTO::getType).contains("farm", "academy");
    assertThat(actual.getBuildings()).extracting(BuildingDTO::getLevel).contains(1, 2);
  }

  @Test
  public void when_requestKingdomWithNonExistentBuildingId_should_returnRequestedResourceNotFoundException() {
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestedResourceNotFoundException.class);
    exceptionRule.expectMessage("Id not found");
    buildingService.getBuildingDTO(999, kingdom);
  }

  @Test
  public void when_requestKingdomWithWrongBuildingId_should_returnForbiddenActionException() {
    LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    Kingdom kingdom1 = new Kingdom();
    kingdom1.setId(998);
    Kingdom kingdom2 = new Kingdom();
    kingdom2.setId(999);
    Building building = new Building(BuildingType.TOWNHALL, 1, kingdom2, currentTime, currentTime);
    building.setId(888);
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.of(building));

    exceptionRule.expect(ForbiddenActionException.class);
    buildingService.getBuildingDTO(999, kingdom1);
  }

  @Test
  public void when_requestKingdomWithProperBuildingId_should_returnValidBuildingDTO() {
    LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
    Kingdom kingdom1 = new Kingdom();
    kingdom1.setId(999);
    Kingdom kingdom2 = new Kingdom();
    kingdom2.setId(999);
    Building building = new Building(BuildingType.TOWNHALL, 1, kingdom2, currentTime, currentTime);
    building.setId(888);
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.of(building));

    BuildingDTO actualDTO = buildingService.getBuildingDTO(999, kingdom1);

    Assert.assertEquals(888, actualDTO.getId());
  }

}