package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.KingdomRepository;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.models.Player;
import javax.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.Assert;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
@Sql("classpath:data.sql")
public class PlayerControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @Test
  public void when_postRegisterWithoutPassword_should_respondBadRequestStatusAndProperJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"\",  "
            + "\"kingdomname\" : \"\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : \"Password is required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postRegisterWithoutUsername_should_respondBadRequestStatusAndProperJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"\",  \"password\" : \"testpassword\",  "
            + "\"kingdomname\" : \"\" }";
    String jsonResponse = "{ \"status\" :  \"error\", \"message\" : \"Username is required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(jsonResponse));
  }

  @Test
  public void when_postRegisterWithoutUsernameAndPassword_should_respondBadRequestStatusAndJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"\",  \"password\" : \"\",  "
            + "\"kingdomname\" : \"\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : "
            + "\"Username and password are required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postRegisterWithNonUniqueUsername_should_respondConflictStatusAndProperJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"existingtestuser\",  \"password\" : \"hellothere\", "
            + "\"kingdomname\" : \"The High Ground\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : "
            + "\"Username is already taken.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isConflict())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postRegisterWithShortPassword_should_respondNotAcceptableStatusAndProperJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"pw\",  "
            + "\"kingdomname\" : \"The High Ground\" }";
    String expectedResponse = "{ \"status\" :  \"error\", \"message\" : "
            + "\"Password must be at least 8 characters.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postRegisterWithUsernamePasswordAndKingdom_should_respondCreatedStatusAndJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"hellothere\",  "
            + "\"kingdomname\" : \"The High Ground\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.id", Matchers.greaterThan(0)))
            .andExpect(jsonPath("$.username").value("obiwan"))
            .andExpect(jsonPath("$.kingdomId").isNumber())
            .andExpect(jsonPath("$.kingdomId", Matchers.greaterThan(0)));
  }

  @Test
  public void when_postRegisterWithUsernamePasswordNoKingdom_should_respondCreatedStatusAndJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"luke\",  \"password\" : \"hellothere\",  "
            + "\"kingdomname\" : \"\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.id", Matchers.greaterThan(0)))
            .andExpect(jsonPath("$.username").value("luke"))
            .andExpect(jsonPath("$.kingdomId").isNumber())
            .andExpect(jsonPath("$.kingdomId", Matchers.greaterThan(0)));
    Assert.assertTrue(kingdomRepository.findFirstByName("luke's kingdom").isPresent());
  }

  @Test
  public void when_getPlayersWithoutDistance_should_respondOkStatusAndJsonWithPlayersIn10units()
      throws Exception {
    Location location = new Location(1, 0,0);
    Kingdom existingkingdom = new Kingdom(1, location);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);

    mockMvc.perform(MockMvcRequestBuilders.get("/players")
                .principal(auth))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.players").exists())
        .andExpect(jsonPath("$.players.length()").value(2))
        .andExpect(jsonPath("$..id", Matchers.containsInAnyOrder(2, 3)));
  }

  @Test
  public void when_getPlayersWithDistance_should_respondOkStatusAndJsonWithPlayersWithinDistance()
      throws Exception {
    Location location = new Location(1, 0,0);
    Kingdom existingkingdom = new Kingdom(1, location);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);

    mockMvc.perform(MockMvcRequestBuilders.get("/players?distance=5")
            .principal(auth))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.players").exists())
        .andExpect(jsonPath("$.players.length()").value(1))
        .andExpect(jsonPath("$..id", Matchers.contains(2)));
  }

}
