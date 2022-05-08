package com.greenfoxacademy.springwebapp.kingdom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
@Sql("classpath:data.sql")
public class KingdomControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private KingdomServiceImpl kingdomService;

  @Autowired
  ObjectMapper mapper;

  @Test
  public void when_getKingdomWithoutKingdomId_should_respondDetailedOwnKingdomJson()
      throws Exception {
    Kingdom existingkingdom = kingdomService.findById(1);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    KingdomResFullDTO dto = kingdomService.convertToKingdomResFullDTO(kingdomService.findById(1));
    String expectedResponse = mapper.writeValueAsString(dto);
    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/").principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_getKingdomWithOwnKingdomId_should_respondDetailedOwnKingdomJson()
      throws Exception {
    int kingdomId = 1;
    Kingdom existingkingdom = kingdomService.findById(kingdomId);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    KingdomResFullDTO dto = kingdomService.convertToKingdomResFullDTO(kingdomService.findById(1));
    String expectedResponse = mapper.writeValueAsString(dto);
    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + kingdomId).principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_getKingdomWithForeignKingdomId_should_respondASortenedKingdomJson()
      throws Exception {
    int myKingdomId = 1;
    int reqKingdomId = 2;
    Kingdom existingkingdom = kingdomService.findById(myKingdomId);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    KingdomResWrappedDTO dto = kingdomService.convertToKingdomResWrappedDTO(kingdomService.findById(reqKingdomId));
    String expectedResponse = mapper.writeValueAsString(dto);
    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + reqKingdomId).principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_getKingdomWithNotExistingKingdomId_should_respondAnErrorDtoJson()
      throws Exception {
    int myKingdomId = 1;
    int reqKingdomId = 999;
    Kingdom existingkingdom = kingdomService.findById(myKingdomId);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    ErrorDTO dto = new ErrorDTO("The requested kingdom is not exist!");
    String expectedResponse = mapper.writeValueAsString(dto);
    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + reqKingdomId).principal(auth))
        .andExpect(status().is(404))
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_getKingdomWithNotNumericId_should_respondAnErrorDtoJson()
      throws Exception {
    int myKingdomId = 1;
    String reqKingdomId = "one";
    Kingdom existingkingdom = kingdomService.findById(myKingdomId);
    Player existingtestuser =
        new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    ErrorDTO dto = new ErrorDTO("Forbidden action");
    String expectedResponse = mapper.writeValueAsString(dto);
    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + reqKingdomId).principal(auth))
        .andExpect(status().is(403))
        .andExpect(content().json(expectedResponse));
  }

}
