package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KingdomServiceTest {

  @Mock
  KingdomRepository kingdomRepository;
  @Mock
  BuildingServiceImpl buildingService;
  @Mock
  ResourceServiceImpl resourceService;
  @Mock
  LocationService locationService;
  @InjectMocks
  KingdomServiceImpl kingdomService;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void when_saveKingdomWithName_should_returnKingdomWithGivenName() {
    Player player = new Player("testuser", "", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());
    when(buildingService.saveAll(any())).thenReturn(buildingListCreator());
    when(resourceService.saveAll(any())).thenReturn(resourceListCreator());
    when(locationService.createLocation()).thenReturn(new Location(0, 0));

    Kingdom created = kingdomService.save("testkingdom", player);

    Assert.assertEquals("testkingdom", created.getName());
  }

  @Test
  public void when_saveKingdomWithoutName_should_returnKingdomWithGeneratedName() {
    Player player = new Player("testuser", "", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());
    when(buildingService.saveAll(any())).thenReturn(buildingListCreator());
    when(resourceService.saveAll(any())).thenReturn(resourceListCreator());
    when(locationService.createLocation()).thenReturn(new Location(0, 0));

    Kingdom created = kingdomService.save("", player);

    Assert.assertEquals("testuser's kingdom", created.getName());
  }

  @Test
  public void when_saveKingdom_should_returnKingdomWithGivenPlayerAndALocation() {
    Player player = new Player("testuser", "", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());
    when(buildingService.saveAll(any())).thenReturn(buildingListCreator());
    when(resourceService.saveAll(any())).thenReturn(resourceListCreator());
    when(locationService.createLocation()).thenReturn(new Location(0, 0));

    Kingdom created = kingdomService.save("testkingdom", player);

    Assert.assertEquals(player, created.getPlayer());
    Assert.assertNotNull(created.getLocation());
  }

  @Test
  public void when_saveKingdom_should_returnKingdomWithGeneratedDefaultPack() {
    Player player = new Player("testuser", "testpassword", null, "", 0);
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());
    when(buildingService.saveAll(any())).thenReturn(buildingListCreator());
    when(resourceService.saveAll(any())).thenReturn(resourceListCreator());

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

  private List<Building> buildingListCreator() {
    List<Building> buildingList = new ArrayList<>();
    buildingList.add(new Building(BuildingType.TOWNHALL, 1, null,  null, null));
    buildingList.add(new Building(BuildingType.FARM, 1, null,  null, null));
    buildingList.add(new Building(BuildingType.MINE, 1, null,  null, null));
    buildingList.add(new Building(BuildingType.ACADEMY, 1, null,  null, null));
    return buildingList;
  }

  private List<Resource> resourceListCreator() {
    List<Resource> resourceList = new ArrayList<>();
    resourceList.add(new Resource(ResourceType.FOOD, 1000, 0, null, null));
    resourceList.add(new Resource(ResourceType.GOLD, 1000, 0, null, null));
    return resourceList;
  }

  @Test
  public void renameKingdomTest() {
    when(kingdomRepository.save(any(Kingdom.class))).then(returnsFirstArg());

    Player player = new Player("testuser", "testpassword", null, "", 0);
    player.setId(1);

    Kingdom initialKingdom = new Kingdom();
    initialKingdom.setName("my initial name");
    initialKingdom.setPlayer(player);
    initialKingdom = kingdomRepository.save(initialKingdom);

    String modifyToThisName = "my modified name";

    KingdomDTO modifiedKingdomAsDTO = kingdomService.renameKingdom(initialKingdom, modifyToThisName);

    Assert.assertEquals(modifiedKingdomAsDTO.getName(), modifyToThisName);
  }

  @Test
  public void convertTest() {
    Player player = new Player("testuser", "testpassword", null, "", 0);
    player.setId(1);

    Kingdom initialKingdom = new Kingdom();
    initialKingdom.setName("my initial name");
    initialKingdom.setPlayer(player);
    initialKingdom.setId(1);

    KingdomDTO kingdomDTO = new KingdomDTO();
    kingdomDTO.setId(initialKingdom.getId());
    kingdomDTO.setUserId(player.getId());
    kingdomDTO.setName(initialKingdom.getName());

    KingdomDTO convertedKingdomDTO = kingdomService.convert(initialKingdom);

    Assert.assertEquals(kingdomDTO, convertedKingdomDTO);

  }

  @Test
  public void checkKingdomPutDtoTest_ThrowsException() {
    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("name is required");
    kingdomService.checkKingdomPutDto(new KingdomPutDTO());
  }

  @Test
  public void checkKingdomPutDto_DoesntThrowException() {
    KingdomPutDTO kingdomPutDTO = new KingdomPutDTO();
    kingdomPutDTO.setName("this is a not empty name");
    kingdomService.checkKingdomPutDto(kingdomPutDTO);
  }

}