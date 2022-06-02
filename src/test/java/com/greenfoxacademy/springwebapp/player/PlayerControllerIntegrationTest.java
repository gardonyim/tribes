package com.greenfoxacademy.springwebapp.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.anyString;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.KingdomRepository;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.mail.EmailService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
  ObjectMapper mapper;

  @MockBean
  private EmailService emailService;

  @Test
  public void when_postRegisterWithoutPassword_should_respondBadRequestStatusAndProperJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"\",  "
            + "\"kingdomname\" : \"\" }";
    ErrorDTO errorDTO = new ErrorDTO("Password is required.");
    String expectedResponse = mapper.writeValueAsString(errorDTO);

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
    ErrorDTO errorDTO = new ErrorDTO("Username is required.");
    String expectedResponse = mapper.writeValueAsString(errorDTO);

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postRegisterWithoutUsernameAndPassword_should_respondBadRequestStatusAndJson()
          throws Exception {
    String jsonRequest = "{ \"username\" : \"\",  \"password\" : \"\",  "
            + "\"kingdomname\" : \"\" }";
    ErrorDTO errorDTO = new ErrorDTO("Password and username are required.");
    String expectedResponse = mapper.writeValueAsString(errorDTO);
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
    ErrorDTO errorDTO = new ErrorDTO("Password must be at least 8 characters.");
    String expectedResponse = mapper.writeValueAsString(errorDTO);

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postRegisterWithUsernamePasswordAndKingdom_should_respondCreatedStatusAndJson()
          throws Exception {
    Mockito.doNothing().when(emailService).sendMessageUsingThymeleafTemplate(anyString(), anyString(), anyString());
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"hellothere\",  "
            + "\"kingdomname\" : \"The High Ground\", "
            + "\"email\" : \"chucknorrrrris@gmail.com\" }";

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

    verify(emailService, times(1)).sendMessageUsingThymeleafTemplate(anyString(), anyString(), anyString());
  }

  @Test
  public void when_postRegisterWithUsernamePasswordNoKingdom_should_respondCreatedStatusAndJson()
          throws Exception {
    Mockito.doNothing().when(emailService).sendMessageUsingThymeleafTemplate(anyString(), anyString(), anyString());
    String jsonRequest = "{ \"username\" : \"luke\",  \"password\" : \"hellothere\",  "
            + "\"kingdomname\" : \"\", "
            + "\"email\" : \"chucknorrrrris@gmail.com\" }";

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

    verify(emailService, times(1)).sendMessageUsingThymeleafTemplate(anyString(), anyString(), anyString());
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
