package com.greenfoxacademy.springwebapp.building;

import com.greenfoxacademy.TestUtils;
import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
<<<<<<< HEAD
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
=======
import com.greenfoxacademy.springwebapp.exceptions.*;
>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.KingdomServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.buildingBuilder;
import static com.greenfoxacademy.TestUtils.defaultKingdom;
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
  private KingdomServiceImpl kingdomService;
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
    Building townhall = new Building(BuildingType.TOWNHALL, 1, kingdom, null, null);
    when(buildingRepository.findFirstByBuildingTypeAndKingdom(any(), any()))
        .thenReturn(Optional.of(townhall));
    when(gameObjectRuleHolder.getBuildingCostMultiplier(anyString(), anyInt())).thenReturn(100);
    when(resourceService.getResourceByKingdomAndType(any(), any())).thenReturn(gold);
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
    when(gameObjectRuleHolder.getBuildingCostMultiplier(anyString(), anyInt())).thenReturn(100);
    when(resourceService.pay(any(), anyInt())).thenReturn(null);
    when(buildingRepository.save(any())).then(returnsFirstArg());
    Mockito.doReturn(new BuildingDTO()).when(buildingService).convertToDTO(any());

    buildingService.addBuilding(typeDTO, kingdom);

    Mockito.verify(buildingService, times(1)).validateAddBuildingRequest(typeDTO, kingdom);
    Mockito.verify(buildingService, times(1)).constructBuilding("academy", 1, kingdom);
    Mockito.verify(gameObjectRuleHolder, times(1)).getBuildingCostMultiplier("academy", 1);
    Mockito.verify(resourceService, times(1)).pay(kingdom, 100);
    Mockito.verify(buildingRepository, times(1)).save(building);
    Mockito.verify(buildingService, times(1)).convertToDTO(building);
  }

  @Test
  public void when_validateModBuildingLevReqWithoutBuildingId_should_throwException() {
    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("Missing parameter(s): buildingId!");

    buildingService.validateModifyBuildingLevelRequest(new BuildingDTO(), new Kingdom(), null);
  }

  @Test
  public void when_validateModBuildingLevReqWithNotExistBuildingId_should_throwException() {
    exceptionRule.expect(RequestedResourceNotFoundException.class);
    exceptionRule.expectMessage("Required building is not exist!");
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.empty());

    buildingService.validateModifyBuildingLevelRequest(new BuildingDTO(), new Kingdom(), 1);
  }

  @Test
  public void when_validateModBuildingLevReqWithForeignBuildingId_should_throwException() {
    exceptionRule.expect(ForbiddenActionException.class);
    exceptionRule.expectMessage("Forbidden action");
    Building reqBuilding = new Building();
<<<<<<< HEAD
    reqBuilding.setKingdom(kingdomBuilder().withId(1).build());
    Kingdom myKingdom = kingdomBuilder().withId(2).build();
=======
    reqBuilding.setKingdom(TestUtils.kingdomBuilder().withId(1).build());
    Kingdom myKingdom = TestUtils.kingdomBuilder().withId(2).build();
>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.of(reqBuilding));

    buildingService.validateModifyBuildingLevelRequest(new BuildingDTO(), myKingdom, 1);
  }

  @Test
  public void when_validateModBuildingLevReqWithHigherLevelThanTownhall_should_throwException() {
    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Cannot build buildings with higher level than the Townhall");
<<<<<<< HEAD
    Kingdom myKingdom = kingdomBuilder().withId(1).build();
    List<Building> buildings = new ArrayList<>();
    Building reqBuilding = buildingBuilder(BuildingType.FARM).withId(2).withLevel(1).build();
    reqBuilding.setKingdom(myKingdom);
    buildings.add(reqBuilding);
    buildings.add(buildingBuilder(BuildingType.TOWNHALL).withId(1).withLevel(1).build());
=======
    Kingdom myKingdom = TestUtils.kingdomBuilder().withId(1).build();
    List<Building> buildings = new ArrayList<>();
    Building reqBuilding = TestUtils.buildingBuilder(BuildingType.FARM).withId(2).withLevel(1).build();
    reqBuilding.setKingdom(myKingdom);
    buildings.add(reqBuilding);
    buildings.add(TestUtils.buildingBuilder(BuildingType.TOWNHALL).withId(1).withLevel(1).build());
>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
    myKingdom.setBuildings(buildings);
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.of(reqBuilding));
    BuildingDTO buildingDTO = new BuildingDTO();
    buildingDTO.setLevel(2);

    buildingService.validateModifyBuildingLevelRequest(buildingDTO, myKingdom, 2);
  }

  @Test
<<<<<<< HEAD
  public void when_validateHasEnoughGoldWithToExpensiveDevelopment_should_returnException() {
    exceptionRule.expect(NotEnoughResourceException.class);
    exceptionRule.expectMessage("Not enough resource");
    Kingdom myKingdom = kingdomBuilder().withId(1).build();
    List<Building> buildings = new ArrayList<>();
    Building reqBuilding = buildingBuilder(BuildingType.FARM).withId(2).withLevel(1).build();
    reqBuilding.setKingdom(myKingdom);
    buildings.add(reqBuilding);
    buildings.add(buildingBuilder(BuildingType.TOWNHALL).withId(1).withLevel(2).build());
=======
  public void when_validateHasEnoughGoldWithToExpensiveDevelpoment_should_returnException() {
    exceptionRule.expect(NotEnoughResourceException.class);
    exceptionRule.expectMessage("Not enough resource");
    Kingdom myKingdom = TestUtils.kingdomBuilder().withId(1).build();
    List<Building> buildings = new ArrayList<>();
    Building reqBuilding = TestUtils.buildingBuilder(BuildingType.FARM).withId(2).withLevel(1).build();
    reqBuilding.setKingdom(myKingdom);
    buildings.add(reqBuilding);
    buildings.add(TestUtils.buildingBuilder(BuildingType.TOWNHALL).withId(1).withLevel(2).build());
>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
    myKingdom.setBuildings(buildings);
    when(buildingRepository.findById(anyInt())).thenReturn(Optional.of(reqBuilding));
    Mockito.doThrow(new NotEnoughResourceException())
        .when(buildingService).validateHasEnoughGold(any(Building.class), anyInt());
    BuildingDTO buildingDTO = new BuildingDTO();
    buildingDTO.setLevel(2);

    buildingService.validateModifyBuildingLevelRequest(buildingDTO, myKingdom, 2);
  }

  @Test
<<<<<<< HEAD
  public void when_modifyBuildingLevelWithAppropriateInput_should_returnDevelopedBuilding() {
    Building modBuildung = buildingBuilder(BuildingType.FARM).withId(2).withLevel(1).build();
    Mockito.doReturn(modBuildung).when(buildingService).validateModifyBuildingLevelRequest(any(), any(), any());
    when(gameObjectRuleHolder.calcNewHP(anyString(), anyInt())).thenReturn(100);
    when(gameObjectRuleHolder.calcCreationTime(anyString(),anyInt(),anyInt())).thenReturn(100);
    when(resourceService.pay(any(), anyInt())).thenReturn(null);
    when(kingdomService.update(any(Kingdom.class))).thenReturn(null);
    Building expectedBuilding = modBuildung;
=======
  public void when_modifyBuildingLevelWithAppropriateInput_should_returnDelvelopedBuilding() {
    //LEVEL KELL BELE, tYPE,
    // KELL KINGDOM
    // kingdomban Gold updat-tel
    Building modifiableBuilding = TestUtils.buildingBuilder(BuildingType.FARM).withId(2).withLevel(1).build();
    Mockito.doReturn(modifiableBuilding).when(buildingService).validateModifyBuildingLevelRequest(any(), any(), any());
    when(gameObjectRuleHolder.calcNewHP(any(), any())).thenReturn(100);
    when(gameObjectRuleHolder.calcCreationTime(any(),anyInt(),anyInt())).thenReturn(100);
    Mockito.doNothing().when(resourceService).pay(any(),anyInt());
    Mockito.doNothing().when(kingdomService).update(any(Kingdom.class));
    Building expectedBuilding = modifiableBuilding;
>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
    expectedBuilding.setStartedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
    expectedBuilding.setFinishedAt(expectedBuilding.getStartedAt().plusSeconds(100));
    expectedBuilding.setHp(expectedBuilding.getHp() + 100);
    BuildingDTO buildingDTO = new BuildingDTO();
    buildingDTO.setLevel(2);
    expectedBuilding.setLevel(buildingDTO.getLevel());
<<<<<<< HEAD
    Building actualBuilding = buildingService.modifyBuildingLevel(buildingDTO, defaultKingdom(), modBuildung.getId());
=======
    //Kingdom myKingdom = TestUtils.defaultKingdom();

    Building actualBuilding = buildingService.modifyBuildingLevel(buildingDTO, TestUtils.defaultKingdom(), modifiableBuilding.getId());

>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
    Assert.assertEquals(expectedBuilding.getLevel(), actualBuilding.getLevel());
    Assert.assertEquals(expectedBuilding.getHp(), actualBuilding.getHp());
    Assert.assertEquals(expectedBuilding.getStartedAt(), actualBuilding.getStartedAt());
    Assert.assertEquals(expectedBuilding.getFinishedAt(), actualBuilding.getFinishedAt());
  }

<<<<<<< HEAD
=======

>>>>>>> dd04e76 (fix(Put Building): fix tests of put endpoint)
}
