package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import javax.transaction.Transactional;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    String expectedResponse = "{ \"id\" :  1, \"level\" : 1, \"hp\" :  20, "
        + "\"attack\" : 10, \"defence\" :  5, \"startedAt\" : 1651648901, \"finishedAt\" :  1651648904 }";

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops/1").principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedResponse));
  }

}
