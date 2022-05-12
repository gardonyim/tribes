package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.TestUtils;
import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.junit.Before;
import org.junit.Test;
import com.greenfoxacademy.springwebapp.exceptions.RequestedForbiddenResourceException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static com.greenfoxacademy.TestUtils.buildingBuilder;
import static com.greenfoxacademy.TestUtils.defaultBuilding;
import static com.greenfoxacademy.TestUtils.defaultKingdom;
import static com.greenfoxacademy.TestUtils.timeOf;
import static com.greenfoxacademy.TestUtils.troopBuilder;
import static com.greenfoxacademy.TestUtils.troopDtoBuilder;
import static com.greenfoxacademy.TestUtils.troopPostDtoBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class TroopServiceTest {

  private BuildingService buildingService;
  private ResourceService resourceService;
  private GameObjectRuleHolder gameObjectRuleHolder;
  private TroopRepository troopRepository;
  private TroopService troopService;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    buildingService = mock(BuildingService.class);
    resourceService = mock(ResourceService.class);
    gameObjectRuleHolder = mock(GameObjectRuleHolder.class);
    troopRepository = mock(TroopRepository.class);
    troopService = Mockito.spy(new TroopServiceImpl(
        buildingService,
        resourceService,
        gameObjectRuleHolder,
        troopRepository));
  }

  @Test
  public void when_getTroopsOfKingdom_should_returnListWithOneTroop() {
    Kingdom kingdom = defaultKingdom();

    TroopsDTO result = troopService.getTroopsOfKingdom(kingdom);

    assertNotEquals(null, result.getTroops());
    assertEquals(kingdom.getTroops().size(), result.getTroops().size());
    assertEquals(kingdom.getTroops().get(0).getId(), result.getTroops().get(0).getId());
  }

  @Test
  public void when_getTroopByIdNotPresent_should_throwException() {
    when(troopRepository.findById(anyInt())).thenReturn(Optional.empty());
    Kingdom kingdom = new Kingdom();

    exceptionRule.expect(RequestedResourceNotFoundException.class);
    exceptionRule.expectMessage("Id not found");
    troopService.getTroopById(kingdom, 1);
  }

  @Test
  public void when_getTroopByIdWithOtherKingdom_should_throwException() {
    Kingdom kingdom1 = new Kingdom();
    kingdom1.setId(999);
    Troop troop = new Troop(1, 0, 0, 0, null, null, kingdom1);
    Kingdom kingdom2 = new Kingdom();
    kingdom2.setId(888);
    when(troopRepository.findById(anyInt())).thenReturn(Optional.of(troop));

    exceptionRule.expect(RequestedForbiddenResourceException.class);
    exceptionRule.expectMessage("Forbidden action");
    troopService.getTroopById(kingdom2, 1);
  }

  @Test
  public void when_getTroopByKingdomAndValidId_should_returnTroop() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(999);
    LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
    Troop troop = new Troop(1, 20, 10, 5, time, time, kingdom);
    troop.setId(999);
    when(troopRepository.findById(999)).thenReturn(Optional.of(troop));

    Troop actual = troopService.getTroopById(kingdom, 999);

    Assert.assertEquals(troop.getId(), actual.getId());
    Assert.assertEquals(troop.getLevel(), actual.getLevel());
    Assert.assertEquals(troop.getHp(), actual.getHp());
    Assert.assertEquals(troop.getAttack(), actual.getAttack());
    Assert.assertEquals(troop.getDefence(), actual.getDefence());
    Assert.assertEquals(troop.getStartedAt(), actual.getStartedAt());
    Assert.assertEquals(troop.getFinishedAt(), actual.getFinishedAt());
  }

  @Test
  public void when_getAcademyWithInvalidId_should_throwException() {
    Mockito.doNothing().when(troopService).checkInputParameters(any(TroopPostDTO.class));
    Building building = buildingBuilder(BuildingType.TOWNHALL).withId(99).build();
    when(buildingService.getBuildingById(anyInt())).thenReturn(building);
    Mockito.doNothing().when(buildingService).checkOwner(any(), anyInt());
    Kingdom kingdom = TestUtils.defaultKingdom();
    TroopPostDTO dto = TestUtils.troopPostDtoBuilder().withBuildingId(99).build();

    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Not a valid academy id");
    troopService.getAcademy(kingdom, dto);
  }

  @Test
  public void when_getAcademyWithValidId_should_returnAcademy() {
    Kingdom kingdom = defaultKingdom();
    TroopPostDTO dto = troopPostDtoBuilder().withBuildingId(99).build();
    Building building = buildingBuilder(BuildingType.ACADEMY).withId(99).build();
    Mockito.doNothing().when(troopService).checkInputParameters(any(TroopPostDTO.class));
    when(buildingService.getBuildingById(anyInt())).thenReturn(building);
    Mockito.doNothing().when(buildingService).checkOwner(any(), anyInt());

    Building actual = troopService.getAcademy(kingdom, dto);

    Assert.assertEquals(99, actual.getId());
    Assert.assertEquals(BuildingType.ACADEMY, actual.getBuildingType());
  }

  @Test
  public void when_setValues_should_returnTroopWithCorrectValues() {
    Troop troop = troopBuilder().withId(99).build();
    when(gameObjectRuleHolder.calcNewHP(anyString(), anyInt())).thenReturn(60);
    when(gameObjectRuleHolder.calcNewAttack(anyString(), anyInt())).thenReturn(30);
    when(gameObjectRuleHolder.calcNewDefence(anyString(), anyInt())).thenReturn(15);
    when(gameObjectRuleHolder.calcCreationTime(anyString(), anyInt(), anyInt())).thenReturn(60);
    Troop expected = troopBuilder().withId(99).withLevel(3).withHP(60).withAttack(30).withDefence(15)
        .startedAt("2022-05-01T00:00:00").finishedAt("2022-05-01T00:01:00").build();
    Troop actual;

    try (MockedStatic<TimeService> timeServiceMockedStatic = Mockito.mockStatic(TimeService.class)) {
      timeServiceMockedStatic.when(() -> TimeService.actualTime()).thenReturn(timeOf("2022-05-01T00:00:00"));
      timeServiceMockedStatic.when(() -> TimeService.timeAtNSecondsLater(anyLong()))
          .thenReturn(timeOf("2022-05-01T00:00:00").plusSeconds(60L));

      actual = troopService.setValues(troop, 1, 3);
    }

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void when_upgradeTroopValid_should_returnTroopDtoWithCorrectValues() {
    Kingdom kingdom = defaultKingdom();
    TroopPostDTO dto = troopPostDtoBuilder().withBuildingId(99).build();
    Building building = defaultBuilding(BuildingType.ACADEMY);
    Troop troop = troopBuilder().withId(99).withLevel(3).build();
    Mockito.doReturn(building).when(troopService).getAcademy(kingdom, dto);
    Mockito.doReturn(troop).when(troopService).getTroopById(kingdom, 99);
    when(gameObjectRuleHolder.calcCreationCost("troop", 1, 3)).thenReturn(50);
    when(resourceService.hasEnoughGold(any(), anyInt())).thenReturn(true);
    when(resourceService.pay(any(), anyInt())).thenReturn(null);
    Mockito.doReturn(troop).when(troopService).setValues(any(Troop.class), anyInt(), anyInt());
    when(troopRepository.save(any())).then(returnsFirstArg());
    TroopDTO expected = troopDtoBuilder(troop).build();

    TroopDTO actual = troopService.upgradeTroop(kingdom, 99, dto);

    Assert.assertEquals(expected, actual);
  }

}
