package com.greenfoxacademy.springwebapp.building;

<<<<<<< HEAD
import static com.greenfoxacademy.TestUtils.defaultPlayer;
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
import static com.greenfoxacademy.springwebapp.utilities.TimeService.toEpochSecond;
=======
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
import static com.greenfoxacademy.TestUtils.resourceBuilder;
>>>>>>> 3a2af28 (refactor(update resource generation): refactor addBuildings and corresponding tests)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import java.util.Collections;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
@Sql("classpath:data.sql")
public class BuildingControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void when_postKingdomBuildingsWithoutType_should_respondBadRequestAndProperJson()
      throws Exception {
    Player existingtestuser = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
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
    Player existingtestuser = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
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
    Player existingtestuser = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
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
    Player testuser2 = playerBuilder().withKingdom(kingdomBuilder().withId(2).build()).build();
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
    Resource gold = resourceBuilder(ResourceType.GOLD).withAmount(50).withGeneration(0).build();
    Kingdom kingdom = kingdomBuilder().withId(3).withResources(Collections.singletonList(gold)).build();
    Player testuser3 = playerBuilder().withKingdom(kingdom).build();
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
  public void when_postKingdomBuildingsValid_should_respondCreatedAndProperJson()
      throws Exception {
    Player testuser4 = playerBuilder().withKingdom(kingdomBuilder().withId(4).build()).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(testuser4, null, null);
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