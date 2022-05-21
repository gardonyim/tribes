package com.greenfoxacademy.springwebapp.kingdom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomBaseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResFullDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomResWrappedDTO;
import com.greenfoxacademy.springwebapp.location.models.LocationDTO;
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

import static com.greenfoxacademy.TestUtils.defaultKingdom;
import static com.greenfoxacademy.TestUtils.defaultLocation;
import static com.greenfoxacademy.TestUtils.kingdomResFullDtoBuilder;
import static com.greenfoxacademy.TestUtils.playerBuilder;
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
  ObjectMapper mapper;

  @Test
  public void when_getKingdomWithoutKingdomId_should_respondDetailedOwnKingdomJson()
      throws Exception {
    Kingdom existingkingdom = defaultKingdom();
    existingkingdom.setLocation(defaultLocation());
    Player existingtestuser =
        playerBuilder().withId(1).withUsername("existingtestuser").withKingdom(existingkingdom).build();
    existingkingdom.setPlayer(existingtestuser);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    KingdomResFullDTO dto = kingdomResFullDtoBuilder(existingkingdom).build();
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/").principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_getKingdomWithKingdomId_should_respondASortenedKingdomJson()
      throws Exception {
    Kingdom existingkingdom1 = defaultKingdom();
    existingkingdom1.setLocation(defaultLocation());
    Player existingtestuser1 =
        playerBuilder().withId(1).withUsername("existingtestuser1").withKingdom(existingkingdom1).build();
    existingkingdom1.setPlayer(existingtestuser1);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser1, null);
    KingdomResWrappedDTO dto = new KingdomResWrappedDTO(new KingdomBaseDTO(
        2, "testkingdom2", 2, new LocationDTO(5, -5)
    ));
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + 2).principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_getKingdomWithNotExistingKingdomId_should_respondAnErrorDtoJson()
      throws Exception {
    int reqKingdomId = 999;
    Player existingtestuser =
        new Player(1, "existingtestuser", null, null, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    ErrorDTO dto = new ErrorDTO("The requested kingdom is not exist!");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + reqKingdomId).principal(auth))
        .andExpect(status().is(404))
        .andExpect(content().json(expectedResponse));
  }

}
