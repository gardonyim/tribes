package com.greenfoxacademy.springwebapp.building;

import static com.greenfoxacademy.TestUtils.resourceBuilder;
import static com.greenfoxacademy.TestUtils.buildingBuilder;
import static com.greenfoxacademy.TestUtils.defaultPlayer;
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
import static com.greenfoxacademy.springwebapp.utilities.TimeService.toEpochSecond;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.PlayerService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
@Sql("classpath:data.sql")
public class BuildingControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PlayerService playerService;

  @Autowired
  ObjectMapper mapper;

  @Test
  public void when_postKingdomBuildingsWithoutType_should_respondBadRequestAndProperJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String jsonRequest = "{ \"type\" : \" \" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : \"Missing parameter(s): type!\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/buildings").principal(auth)
        .contentType("application/json")
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postKingdomBuildingsWithInvalidType_should_respondNotAcceptableAndProperJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String jsonRequest = "{ \"type\" : \"whatever\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : \"Invalid building type\" }";


    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/buildings").principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postKingdomBuildingsWithTownhallAsType_should_respondBadRequestAndProperJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String jsonRequest = "{ \"type\" : \"townhall\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : "
        + "\"There must only be one Townhall in a kingdom\" }";


    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/buildings").principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isConflict())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postKingdomBuildingsAndNoTownhallInKingdom_should_respondNotAcceptableAndProperJson()
      throws Exception {
    Kingdom testkingdom2 = new Kingdom(2, new Location());
    Player testuser2 =
        new Player(2, "testuser2", null, testkingdom2, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(testuser2, null);
    String jsonRequest = "{ \"type\" : \"mine\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : "
        + "\"Cannot build buildings with higher level than the Townhall\" }";


    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/buildings").principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postKingdomBuildingsAndNoSufficientResources_should_respondConflictAndProperJson()
      throws Exception {
    Kingdom testkingdom3 = new Kingdom(3, new Location());
    Player testuser3 =
        new Player(3, "testuser3", null, testkingdom3, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(testuser3, null);
    String jsonRequest = "{ \"type\" : \"farm\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : \"Not enough resources\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/buildings").principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isConflict())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postKingdomBuildingsValid_should_respondOkStatusAndProperJson()
      throws Exception {
    Kingdom testkingdom4 = new Kingdom(4, new Location());
    Player testuser4 =
        new Player(4, "testuser4", null, testkingdom4, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(testuser4, null);
    String jsonRequest = "{ \"type\" : \"academy\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/buildings").principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.type").value("academy"))
        .andExpect(jsonPath("$.level").value(1))
        .andExpect(jsonPath("$.hp").value(150))
        .andExpect(jsonPath("$.startedAt").value(toEpochSecond(TimeService.actualTime())))
        .andExpect(jsonPath("$.finishedAt").value(toEpochSecond(TimeService.timeAtNSecondsLater(90))));
  }

  @Test
  public void when_putKingdomBuildingsWithNotExistBuildingId_should_respondStatus404AndProperErrorDtoInJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    Integer buildingId = 100;
    String jsonRequest = "{ \"level\" : \"2\" }";
    ErrorDTO dto = new ErrorDTO("Required building is not exist!");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().is(404))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_putKingdomBuildingsWithNotOwnBuildingId_should_respondStatus403AndProperErrorDtoInJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    Integer buildingId = 3;
    String jsonRequest = "{ \"level\" : \"2\" }";
    ErrorDTO dto = new ErrorDTO("Forbidden action");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().is(403))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_putKingdomBuildingsWithOwnBuildingIdAndToHighLevel_should_respondStatus406AndProperErrorDtoInJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    existingkingdom.setBuildings(Arrays.asList(buildingBuilder(BuildingType.TOWNHALL).withLevel(1).build()));
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "5";
    String jsonRequest = "{ \"level\" : \"3\" }";
    ErrorDTO dto = new ErrorDTO("Cannot build buildings with higher level than the Townhall");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().is(406))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_putKingdomBuildingsWithOwnBuildingIdAndToExpensLevel_should_respondStatus409AndProperErrorDtoInJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    existingkingdom.setBuildings(Arrays.asList(buildingBuilder(BuildingType.TOWNHALL).withLevel(1).build()));
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "1";
    String jsonRequest = "{ \"level\" : \"5\" }";
    ErrorDTO dto = new ErrorDTO("Not enough resource");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().is(409))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_putKingdomBuildingsWithOwnBuildingIdAndProperLevel_should_respondStatus200AndPropBuildingDtoInJson()
      throws Exception {
    Kingdom existingkingdom = new Kingdom(5, new Location());
    Resource money = resourceBuilder(ResourceType.GOLD).withAmount(1000).build();
    money.setKingdom(existingkingdom);
    existingkingdom.setResources(Arrays.asList(money));
    Building townhall = buildingBuilder(BuildingType.TOWNHALL).withLevel(2).finishedAt("2022-01-01T00:00:00").build();
    townhall.setKingdom(existingkingdom);
    existingkingdom.setBuildings(Arrays.asList(townhall));
    Player existingtestuser = new Player(5, "existingtestuser", null, existingkingdom, null, 0);
    existingkingdom.setPlayer(existingtestuser);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "11";
    String jsonRequest = "{ \"level\" : \"2\" }";

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json").content(jsonRequest)).andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(11)).andExpect(jsonPath("$.type").value("academy"))
        .andExpect(jsonPath("$.level").value(2)).andExpect(jsonPath("$.hp").value(300));
  }

  @Test
  public void when_requestBuildingWithoutId_should_returnListOfBuildingsDTO() throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken(defaultPlayer(), null);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/buildings")
            .principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.buildings").exists())
        .andExpect(jsonPath("$.buildings").isArray())
        .andExpect(jsonPath("$.buildings[0].id").isNumber())
        .andExpect(jsonPath("$.buildings[0].type").value("townhall"))
        .andExpect(jsonPath("$.buildings[0].level").isNumber())
        .andExpect(jsonPath("$.buildings[0].hp").isNumber());
  }

  @Test
  public void when_requestBuildingWithId_should_returnValidBuildingDTO() throws Exception {
    Kingdom kingdom = kingdomBuilder().withId(1).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Building building = kingdom.getBuildings().get(0);
    building.setId(1);
    building.setKingdom(player.getKingdom());
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/buildings/1")
                .principal(auth))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.type").value("townhall"))
            .andExpect(jsonPath("$.level").value(1))
            .andExpect(jsonPath("$.hp").value(200))
            .andExpect(jsonPath("$.startedAt").isNumber())
            .andExpect(jsonPath("$.finishedAt").isNumber());
  }

  @Test
  public void when_requestBuildingInOtherKingdom_should_returnForbiddenRequestException()
      throws Exception {
    Kingdom kingdom = kingdomBuilder().withId(2).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Building building = kingdom.getBuildings().get(0);
    building.setId(1);
    building.setKingdom(player.getKingdom());
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    String expectedResponse = "{ \"status\": \"error\", \"message\": \"Forbidden action\" }";

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/buildings/1")
            .principal(auth))
            .andExpect(status().is(403))
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_requestBuildingWithNonExistingId_should_returnRequestedResourceNotFoundException()
      throws Exception {
    Kingdom kingdom = kingdomBuilder().withId(1).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Building building = kingdom.getBuildings().get(0);
    building.setId(999);
    building.setKingdom(player.getKingdom());
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : \"Id not found\" }";

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/buildings/999")
            .principal(auth))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expectedResponse));
  }

}
