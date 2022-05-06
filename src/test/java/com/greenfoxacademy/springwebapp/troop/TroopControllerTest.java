package com.greenfoxacademy.springwebapp.troop;

import com.google.gson.Gson;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.ErrorDTO;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static com.greenfoxacademy.TestUtils.defaultPlayer;
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
import static org.hamcrest.Matchers.is;
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
public class TroopControllerTest {

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

}
