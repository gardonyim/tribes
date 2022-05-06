package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.gamesettings.model.GameObjectRuleHolder;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
import org.junit.Before;
import org.junit.Test;

import static com.greenfoxacademy.TestUtils.defaultKingdom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class TroopServiceTest {

  private BuildingService buildingService;
  private ResourceService resourceService;
  private GameObjectRuleHolder gameObjectRuleHolder;
  private TroopRepository troopRepository;
  private TroopService troopService;

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

}
