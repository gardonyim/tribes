package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestedResourceNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomBaseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.location.LocationServiceImpl;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceServiceImpl;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceDTO;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.troop.TroopServiceImpl;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.greenfoxacademy.TestUtils.defaultKingdom;
import static com.greenfoxacademy.TestUtils.defaultLocation;
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.kingdomResFullDtoBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
  LocationServiceImpl locationService;
  @Mock
  TroopServiceImpl troopService;
  @Spy
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

  @Test
  public void when_convertKingdom_should_returnProperKingdomResFullDTO() {
    LocalDateTime ldt = LocalDateTime.now();
    long ldte = ldt.toEpochSecond(ZoneOffset.UTC);
    Kingdom kdm = createTestKingdom(ldt);
    when(buildingService.convertToDTOs(any())).thenReturn(Arrays.asList(new BuildingDTO(0,
            kdm.getBuildings().get(0).getBuildingType(), kdm.getBuildings().get(0).getLevel(),
            kdm.getBuildings().get(0).getHp(), ldte, ldte)));
    when(resourceService.convertToResourceDTOs((any())))
            .thenReturn(Arrays.asList(new ResourceDTO("gold", 100, 1, ldte)));
    when(troopService.convert(any(List.class)))
            .thenReturn(Arrays.asList(new TroopDTO(null, 1, 1, 1, 1, ldte, ldte)));
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

  @Test
  public void when_findKingdomByExistId_should_returnProperKingdom() {
    int kingdomId = 1;
    Kingdom kingdom = kingdomBuilder().withId(kingdomId).build();
    when(kingdomRepository.findById(anyInt())).thenReturn(Optional.of(kingdom));

    Kingdom actualKingdom = kingdomService.findById(kingdomId);

    Assertions.assertNotNull(actualKingdom);
  }

  @Test
  public void when_findKingdomByNotExistId_should_throwException() {
    when(kingdomRepository.findById(anyInt())).thenReturn(Optional.empty());
    exceptionRule.expect(RequestedResourceNotFoundException.class);
    exceptionRule.expectMessage("The requested kingdom is not exist!");

    kingdomService.findById(999);
  }

  @Test
  public void when_fetchKingdomDataWithKingdomObject_should_returnProperKingdomResfullDto() {
    Kingdom existingkingdom = defaultKingdom();
    existingkingdom.setLocation(defaultLocation());
    Player existingtestuser =
            playerBuilder().withId(1).withUsername("existingtestuser").withKingdom(existingkingdom).build();
    existingkingdom.setPlayer(existingtestuser);
    KingdomResFullDTO expectedDto = kingdomResFullDtoBuilder(existingkingdom).build();
    Mockito.doReturn(expectedDto).when(kingdomService).convertToKingdomResFullDTO(any(Kingdom.class));

    KingdomResFullDTO actualDto = kingdomService.fetchKingdomData(existingkingdom);

    Assert.assertEquals(expectedDto, actualDto);
  }

  @Test
  public void when_fetchKingdomDataWithKingdomId_should_returnProperKingdomResfullDto() {
    Kingdom existingkingdom = kingdomBuilder().withId(2).withName("testkingdom2").build();
    existingkingdom.setLocation(new Location(5, -5));
    Player existingtestuser =
            playerBuilder().withId(2).withUsername("existingtestuser").withKingdom(existingkingdom).build();
    existingkingdom.setPlayer(existingtestuser);
    KingdomResWrappedDTO expectedDto = new KingdomResWrappedDTO(new KingdomBaseDTO(
            2, "testkingdom2", 2, new LocationDTO(5, -5)));
    Mockito.doReturn(new Kingdom()).when(kingdomService).findById(anyInt());
    Mockito.doReturn(expectedDto).when(kingdomService).convertToKingdomResWrappedDTO(any(Kingdom.class));

    KingdomResWrappedDTO actualDto = kingdomService.fetchKingdomData(2);

    Assert.assertEquals(expectedDto, actualDto);
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
    long ldt = testKingdom.getBuildings().get(0).getFinishedAt().toEpochSecond(ZoneOffset.UTC);
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
    expected.setTroops(Arrays.asList(new TroopDTO(null, 1, 1, 1, 1, ldt, ldt)));
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
    buildingList.add(new Building(BuildingType.TOWNHALL, 1, null, null, null));
    buildingList.add(new Building(BuildingType.FARM, 1, null, null, null));
    buildingList.add(new Building(BuildingType.MINE, 1, null, null, null));
    buildingList.add(new Building(BuildingType.ACADEMY, 1, null, null, null));
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

    Kingdom existingkingdom = new Kingdom(1, new Location());
    existingkingdom.setName("my initial name");
    Player existingtestuser =
            new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    existingkingdom.setPlayer(existingtestuser);

    existingkingdom = kingdomRepository.save(existingkingdom);

    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    KingdomPutDTO kingdomPutDTO = new KingdomPutDTO();
    kingdomPutDTO.setName("my modified name");
    KingdomResFullDTO modifiedKingdomAsDTO = kingdomService.renameKingdom(existingkingdom, kingdomPutDTO, auth);

    Assert.assertEquals(kingdomPutDTO.getName(), modifiedKingdomAsDTO.getName());
  }

  @Test
  public void convertTest() {
    when(locationService.convertToLocationDTO(any())).thenReturn(new LocationDTO(1, 1));
    Player player = new Player("testuser", "testpassword", null, "", 0);
    player.setId(1);
    Kingdom initialKingdom = new Kingdom();
    initialKingdom.setName("my initial name");
    initialKingdom.setPlayer(player);
    initialKingdom.setId(1);
    Location location = new Location(1, 1);
    initialKingdom.setLocation(location);
    KingdomResFullDTO kingdomDTO = new KingdomResFullDTO();
    kingdomDTO.setKingdomId(initialKingdom.getId());
    kingdomDTO.setUserId(player.getId());
    kingdomDTO.setName(initialKingdom.getName());
    kingdomDTO.setLocation(new LocationDTO(1, 1));
    KingdomResFullDTO convertedKingdomDTO = kingdomService.convertToKingdomResFullDTO(initialKingdom);

    Assert.assertEquals(kingdomDTO, convertedKingdomDTO);
  }

  @Test
  public void checkKingdomPutDtoTest_ThrowsException() {
    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("name is required");
    kingdomService.checkKingdomPutDto(new KingdomPutDTO());
  }
}