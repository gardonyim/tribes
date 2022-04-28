package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KingdomServiceTest {

  @Mock
  KingdomRepository kingdomRepository;

  @InjectMocks
  KingdomServiceImpl kingdomService;

  @Test
  public void when_saveKingdomWithName_should_returnKingdomWithGivenName() {
    Player player = new Player("testuser", "", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());

    Kingdom created = kingdomService.save("testkingdom", player);

    Assert.assertEquals("testkingdom", created.getName());
  }

  @Test
  public void when_saveKingdomWithoutName_should_returnKingdomWithGeneratedName() {
    Player player = new Player("testuser", "", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());

    Kingdom created = kingdomService.save("", player);

    Assert.assertEquals("testuser's kingdom", created.getName());
  }

  @Test
  public void when_saveKingdom_should_returnKingdomWithGivenPlayer() {
    Player player = new Player("testuser", "", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());

    Kingdom created = kingdomService.save("testkingdom", player);

    Assert.assertEquals(player, created.getPlayer());
  }

  @Test
  public void when_saveKingdom_should_returnKingdomWithGeneratedDefaultPack() {
    Player player = new Player("testuser", "testpassword", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());

    Kingdom testKingdomWithDefaultPack = kingdomService.save("testkingdom", player);

    Assert.assertEquals(4, testKingdomWithDefaultPack.getBuildings().size());
    Assert.assertEquals(BuildingType.TOWNHALL, testKingdomWithDefaultPack.getBuildings().get(0).getBuildingType());
    Assert.assertEquals(BuildingType.FARM, testKingdomWithDefaultPack.getBuildings().get(1).getBuildingType());
    Assert.assertEquals(BuildingType.MINE, testKingdomWithDefaultPack.getBuildings().get(2).getBuildingType());
    Assert.assertEquals(BuildingType.ACADEMY, testKingdomWithDefaultPack.getBuildings().get(3).getBuildingType());

    Assert.assertEquals(2, testKingdomWithDefaultPack.getResources().size());
    Assert.assertEquals(ResourceType.FOOD, testKingdomWithDefaultPack.getResources().get(0).getResourceType());
    Assert.assertEquals(ResourceType.GOLD, testKingdomWithDefaultPack.getResources().get(1).getResourceType());
    Assert.assertEquals(1000, testKingdomWithDefaultPack.getResources().get(0).getAmount());
    Assert.assertEquals(1000, testKingdomWithDefaultPack.getResources().get(1).getAmount());
  }
}