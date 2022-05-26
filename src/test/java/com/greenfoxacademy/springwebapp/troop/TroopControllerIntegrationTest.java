package com.greenfoxacademy.springwebapp.troop;

import com.google.gson.Gson;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.ErrorDTO;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.greenfoxacademy.TestUtils.defaultPlayer;
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
import static com.greenfoxacademy.TestUtils.resourceBuilder;
import static com.greenfoxacademy.TestUtils.troopBuilder;
import static com.greenfoxacademy.TestUtils.troopDtoBuilder;
import static com.greenfoxacademy.TestUtils.troopPostDtoBuilder;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
@Sql("classpath:data.sql")
public class TroopControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @SpyBean
  ResourceService resourceService;
  private Player playerWithTroops;
  private Player playerWithoutTroops;
  private Troop troop;
  Gson gson = new Gson();

  @Before
  public void setUp() throws Exception {
    playerWithTroops = defaultPlayer();
    troop = playerWithTroops.getKingdom().getTroops().get(0);
    playerWithoutTroops = playerBuilder().withKingdom(kingdomBuilder().withTroops(new ArrayList<>()).build()).build();
  }

  @Test
  public void test_getTroopsById_should_respondOkResponseStatusWithTroops()
      throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken(playerWithTroops, null, null);
    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops")
            .principal(auth))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.troops").isArray())
        .andExpect(jsonPath("$.troops[0].id", is(troop.getId())))
        .andExpect(jsonPath("$.troops[0].level", is(troop.getLevel())))
        .andExpect(jsonPath("$.troops[0].hp", is(troop.getHp())))
        .andExpect(jsonPath("$.troops[0].attack", is(troop.getAttack())))
        .andExpect(jsonPath("$.troops[0].defence", is(troop.getDefence())))
        .andExpect(jsonPath("$.troops[0].startedAt",
            is((int) troop.getStartedAt().toEpochSecond(ZoneOffset.UTC))))
        .andExpect(jsonPath("$.troops[0].finishedAt",
            is((int) troop.getFinishedAt().toEpochSecond(ZoneOffset.UTC))));
  }

  @Test
  public void test_getTroopsById_should_respondOkResponseStatusAndNoTroops()
      throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken(playerWithoutTroops, null, null);
    String expectedResponse = "{\"troops\":[]}";

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops")
            .principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void test_postTroops_should_respond201ResponseStatusWithTroop()
      throws Exception {
    Player player = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null, null);
    TroopPostDTO troopPostDTO = new TroopPostDTO(5);

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.level").value(1))
        .andExpect(jsonPath("$.hp").value(20))
        .andExpect(jsonPath("$.attack").value(10))
        .andExpect(jsonPath("$.defence").value(5));
  }

  @Test
  public void test_postTroops_should_respond400ResponseStatusWithError_when_noBuildingIdIsGiven()
      throws Exception {
    Player player = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null, null);
    TroopPostDTO troopPostDTO = new TroopPostDTO();
    ErrorDTO expected = new ErrorDTO("buildingId must be present");

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().is(400))
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void test_postTroops_should_respond403ResponseStatusWithError_when_otherKingdomsBuildingIsGiven()
      throws Exception {
    Player player = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null, null);
    TroopPostDTO troopPostDTO = new TroopPostDTO(7);
    ErrorDTO expected = new ErrorDTO("Forbidden action");

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().is(403))
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void test_postTroops_should_respond406ResponseStatusWithError_when_notAcademyIdIsGiven()
      throws Exception {
    Player player = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null, null);
    TroopPostDTO troopPostDTO = new TroopPostDTO(1);
    ErrorDTO expected = new ErrorDTO("Not a valid academy id");

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().is(406))
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void test_postTroops_should_respond409ResponseStatusWithError_when_notEnoughResources()
      throws Exception {
    Player player = playerBuilder().withKingdom(kingdomBuilder().withId(1).build()).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null, null);
    TroopPostDTO troopPostDTO = new TroopPostDTO(5);
    doThrow(new NotEnoughResourceException()).when(resourceService).hasEnoughGold(any(Kingdom.class), anyInt());
    ErrorDTO expected = new ErrorDTO("Not enough resource");

    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().is(409))
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_getKingdomTroopsIdWithNonExistentId_should_respondWithStatusNotFoundAndJson()
      throws Exception {
    Kingdom kingdom = new Kingdom();
    Player player = new Player(null, null, kingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    String expectedBadResponse = "{ \"status\" :  \"error\", \"message\" : \"Id not found\" }";

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops/999").principal(auth))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expectedBadResponse));
  }

  @Test
  public void when_getKingdomTroopsIdWithForbiddenId_should_respondWithStatusForbiddenAndJson()
      throws Exception {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(2);
    Player player = new Player(null, null, kingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    String expectedBadResponse = "{ \"status\" :  \"error\", \"message\" : \"Forbidden action\" }";

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops/1").principal(auth))
        .andExpect(status().isForbidden())
        .andExpect(content().json(expectedBadResponse));
  }

  @Test
  public void when_getKingdomTroopsIdValid_should_respondWithOkStatusAndProperJson()
      throws Exception {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1);
    Player player = new Player(null, null, kingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops/1").principal(auth))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(1)))
        .andExpect(jsonPath("$.attack", is(1)))
        .andExpect(jsonPath("$.defence", is(1)))
        .andExpect(jsonPath("$.startedAt", is(both(greaterThan(0)).and(lessThan(Integer.MAX_VALUE)))))
        .andExpect(jsonPath("$.finishedAt", is(both(greaterThan(0)).and(lessThan(Integer.MAX_VALUE)))));
  }

  @Test
  public void when_putKingdomTroopsIdWithoutBuildingId_should_respondBadRequestAndProperJson()
      throws Exception {
    Player player = defaultPlayer();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(null).build();
    ErrorDTO expected = new ErrorDTO("buildingId must be present");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(gson.toJson(troopPostDTO))
        .principal(auth))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdWithBuildingIdBelongToAnotherKingdom_should_respondForbiddenAndProperJson()
      throws Exception {
    Player player = defaultPlayer();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(5).build();
    ErrorDTO expected = new ErrorDTO("Forbidden action");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isForbidden())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdWithTroopBelongToAnotherKingdom_should_respondForbiddenAndProperJson()
      throws Exception {
    Kingdom kingdom = kingdomBuilder().withId(2).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(7).build();
    ErrorDTO expected = new ErrorDTO("Forbidden action");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isForbidden())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdWithBuildingIdInvalid_should_respondNotFoundAndProperJson()
      throws Exception {
    Player player = defaultPlayer();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(99).build();
    ErrorDTO expected = new ErrorDTO("No such building.");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isNotFound())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdWithIdInvalid_should_respondNotFoundAndProperJson()
      throws Exception {
    Kingdom kingdom = kingdomBuilder().withId(2).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(7).build();
    ErrorDTO expected = new ErrorDTO("Id not found");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isNotFound())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdWithBuildingIdNotAnAcademy_should_respondNotAcceptableAndProperJson()
      throws Exception {
    Kingdom kingdom = kingdomBuilder().withId(1).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(1).build();
    ErrorDTO expected = new ErrorDTO("Not a valid academy id");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdWithNotEnoughResources_should_respondConflictAndProperJson()
      throws Exception {
    Resource gold = resourceBuilder(ResourceType.GOLD).withId(1).withAmount(50).withGeneration(0).build();
    Kingdom kingdom = kingdomBuilder().withId(3).withResources(Collections.singletonList(gold)).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(8).build();
    ErrorDTO expected = new ErrorDTO("Not enough resource");

    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isConflict())
        .andExpect(content().json(gson.toJson(expected)));
  }

  @Test
  public void when_putKingdomTroopsIdValid_should_respondOkStatusAndProperJson() throws Exception {
    Resource gold = resourceBuilder(ResourceType.GOLD).withId(2).withAmount(150).withGeneration(0).build();
    Kingdom kingdom = kingdomBuilder().withId(4).withResources(Collections.singletonList(gold)).build();
    Player player = playerBuilder().withKingdom(kingdom).build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    TroopPostDTO troopPostDTO = troopPostDtoBuilder().withBuildingId(9).build();
    Troop troop = troopBuilder().withId(3).withLevel(2).withHP(40).withAttack(20).withDefence(10).build();
    TroopDTO expected = troopDtoBuilder(troop)
        .withStartedAt(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC))
        .withFinishedAt(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(30L).toEpochSecond(ZoneOffset.UTC)).build();
    mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/3")
            .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(troopPostDTO))
            .principal(auth))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(3)))
        .andExpect(jsonPath("$.level", is(2)))
        .andExpect(jsonPath("$.hp", is(40)))
        .andExpect(jsonPath("$.attack", is(20)))
        .andExpect(jsonPath("$.defence", is(10)));
  }

}
