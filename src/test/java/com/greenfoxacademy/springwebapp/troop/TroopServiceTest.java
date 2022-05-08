package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
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

import static com.greenfoxacademy.TestUtils.defaultKingdom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
    troopService = new TroopServiceImpl(buildingService, resourceService, gameObjectRuleHolder, troopRepository);
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
    when(troopRepository.findById(anyInt())).thenReturn(Optional.of(troop));

    Troop actual = troopService.getTroopById(kingdom, 999);

    Assert.assertEquals(troop.getId(), actual.getId());
    Assert.assertEquals(troop.getLevel(), actual.getLevel());
    Assert.assertEquals(troop.getHp(), actual.getHp());
    Assert.assertEquals(troop.getAttack(), actual.getAttack());
    Assert.assertEquals(troop.getDefence(), actual.getDefence());
    Assert.assertEquals(troop.getStartedAt(), actual.getStartedAt());
    Assert.assertEquals(troop.getFinishedAt(), actual.getFinishedAt());
  }
}
