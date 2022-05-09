package com.greenfoxacademy.springwebapp.building;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.PlayerService;
import com.greenfoxacademy.springwebapp.player.models.Player;
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
        .andExpect(jsonPath("$.startedAt").value(TimeService.toEpochSecond(TimeService.actualTime())))
        .andExpect(jsonPath("$.finishedAt").value(TimeService.toEpochSecond(TimeService.timeAtNSecondsLater(90))));
  }

  @Test
  public void when_putKingdomBuildingsWithoutBuildingId_should_respondStatus400AndProperErrorDtoInJson()
      throws Exception {
    Player existingtestuser = playerService.findFirstByUsername("existingtestuser").get();
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "";
    String jsonRequest = "{ \"level\" : \"2\" }";
    ErrorDTO dto = new ErrorDTO("Missing parameter(s): buildingId!");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
        .contentType("application/json")
        .content(jsonRequest))
        .andExpect(status().is(400))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_putKingdomBuildingsWithNonIntegerBuildingId_should_respondStatus400AndProperErrorDtoInJson()
      throws Exception {
    Player existingtestuser = playerService.findFirstByUsername("existingtestuser").get();
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "one";
    String jsonRequest = "{ \"level\" : \"2\" }";
    ErrorDTO dto = new ErrorDTO("Missing parameter(s): buildingId!");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().is(400))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_putKingdomBuildingsWithNotOwnBuildingId_should_respondStatus403AndProperErrorDtoInJson()
      throws Exception {
    Player existingtestuser = playerService.findFirstByUsername("existingtestuser").get();
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "10";
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
    Player existingtestuser = playerService.findFirstByUsername("existingtestuser").get();
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
    Player existingtestuser = playerService.findFirstByUsername("existingtestuser").get();
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "1";
    String jsonRequest = "{ \"level\" : \"20\" }";
    ErrorDTO dto = new ErrorDTO("Not enough resources");
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
    Player existingtestuser = playerService.findFirstByUsername("existingtestuser").get();
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    String buildingId = "1";
    String jsonRequest = "{ \"level\" : \"2\" }";
    BuildingDTO dto = new BuildingDTO();
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/" + buildingId).principal(auth)
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().is(200))
        .andExpect(content().json(expectedResponse));
  }





}
