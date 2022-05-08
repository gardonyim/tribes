package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomBaseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.greenfoxacademy.springwebapp.troop.TroopServiceImpl;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
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
  @Mock
  BuildingServiceImpl buildingService;
  @Mock
  ResourceServiceImpl resourceService;
  @Mock
  LocationService locationService;
  @Mock
  TroopServiceImpl troopService;
  @InjectMocks
  KingdomServiceImpl kingdomService;

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

  @Test
  public void when_convertKingdom_should_returnProperKingdomResFullDTO() {
    LocalDateTime ldt = LocalDateTime.now();
    long ldte = TimeService.toEpochSecond(ldt);
    Kingdom kdm = createTestKingdom(ldt);
    when(buildingService.convertToDTO(any())).thenReturn(new BuildingDTO(0,
        kdm.getBuildings().get(0).getBuildingType(), kdm.getBuildings().get(0).getLevel(),
        kdm.getBuildings().get(0).getHp(), ldte, ldte));
    when(resourceService.convertToResourceDTO((any())))
        .thenReturn(new ResourceDTO("gold", 100, 1, ldte));
    when(troopService.convert((any()))).thenReturn(new TroopDTO(null, 1,1,1,1, ldte, ldte));
    when(locationService.convertToLocationDTO((any())))
        .thenReturn(new LocationDTO(kdm.getLocation().getxcoordinate(), kdm.getLocation().getxcoordinate()));
    KingdomResFullDTO expectedKingdomResFullDTO = createExpectedKingdomResFullDTO(kdm);
    KingdomResFullDTO actualKingdomResFullDTO = kingdomService.convertToKingdomResFullDTO(kdm);
    Assert.assertEquals(expectedKingdomResFullDTO, actualKingdomResFullDTO);
  }

  @Test
  public void when_convertKingdom_should_returnProperKingdomResWrappedDTO() {
    Kingdom kdm = createTestKingdom(null);
    when(locationService.convertToLocationDTO((any())))
        .thenReturn(new LocationDTO(kdm.getLocation().getxcoordinate(), kdm.getLocation().getxcoordinate()));
    KingdomResWrappedDTO expectedKingdomResWrappedDTO = createExpectedKingdomResWrappedDTO(kdm);
    KingdomResWrappedDTO actualKingdomResWrappedDTO = kingdomService.convertToKingdomResWrappedDTO(kdm);
    Assert.assertEquals(expectedKingdomResWrappedDTO, actualKingdomResWrappedDTO);
  }

  private Kingdom createTestKingdom(LocalDateTime ldt) {
    Kingdom kdm = new Kingdom();
    kdm.setPlayer(new Player(1, "Bela", "verysecret", kdm, "", 100));
    kdm.setBuildings(new ArrayList<>(Arrays.asList(new Building(BuildingType.FARM, 1, kdm, ldt, ldt))));
    kdm.setResources(new ArrayList<>(Arrays.asList(new Resource(ResourceType.GOLD, 100, 1, ldt, kdm))));
    kdm.setTroops(new ArrayList<>(Arrays.asList(new Troop(1, 1, 1, 1, ldt, ldt, kdm))));
    kdm.setName("Magyarisztan");
    kdm.setId(1);
    kdm.setLocation(new Location(1, 100, 100));
    return kdm;
  }

  private KingdomResFullDTO createExpectedKingdomResFullDTO(Kingdom testKingdom) {
    long ldt = TimeService.toEpochSecond(testKingdom.getBuildings().get(0).getFinishedAt());
    KingdomResFullDTO expected = new KingdomResFullDTO();
    expected.setKingdomId(testKingdom.getId());
    expected.setName(testKingdom.getName());
    expected.setUserId(testKingdom.getId());
    expected.setLocation(
        new LocationDTO(testKingdom.getLocation().getxcoordinate(), testKingdom.getLocation().getxcoordinate()));
    expected.setBuildings(Arrays.asList(new BuildingDTO(
        0, testKingdom.getBuildings().get(0).getBuildingType(),
        testKingdom.getBuildings().get(0).getLevel(), testKingdom.getBuildings().get(0).getHp(), ldt, ldt)));
    expected.setResources(Arrays.asList(new ResourceDTO("gold", 100, 1, ldt)));
    expected.setTroops(Arrays.asList(new TroopDTO(null, 1,1,1,1, ldt, ldt)));
    return expected;
  }

  private KingdomResWrappedDTO createExpectedKingdomResWrappedDTO(Kingdom testKingdom) {
    KingdomBaseDTO kingdomBaseDTO = new KingdomBaseDTO(
        testKingdom.getId(),
        testKingdom.getName(),
        testKingdom.getId(),
        new LocationDTO(testKingdom.getLocation().getxcoordinate(), testKingdom.getLocation().getxcoordinate())
    );
    return new KingdomResWrappedDTO(kingdomBaseDTO);
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

}