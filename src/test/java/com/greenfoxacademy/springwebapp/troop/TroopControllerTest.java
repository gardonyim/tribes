package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
public class TroopControllerTest {

  @Autowired
  private MockMvc mockMvc;
  private Player player;

  @Before
  public void setUp() throws Exception {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1);
    player = new Player();
    player.setId(1);
    player.setKingdom(kingdom);
  }

  @Test
  public void when_getKingdomTroopsById_should_respondOkResponseStatusWithTroops()
          throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops")
            .principal((Principal) player))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.troops").isArray())
            .andExpect(jsonPath("$.troops[0].id", is(1)))
            .andExpect(jsonPath("$.troops[0].level", is(1)))
            .andExpect(jsonPath("$.troops[0].hp", is(1)))
            .andExpect(jsonPath("$.troops[0].attack", is(1)))
            .andExpect(jsonPath("$.troops[0].defence", is(1)))
            .andExpect(jsonPath("$.troops[0].startedAt").isNumber())
            .andExpect(jsonPath("$.troops[0].finishedAt").isNumber());
  }

//  @Sql({"classpath:data.sql"})
//  @Test
//  public void when_getKingdomTroopsById_should_respondOkResponseStatusAndNoTroops()
//          throws Exception {
//    String expectedResponse = "{\"troops\":[]}";
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops")
//            .header("Authorization", "Bearer " + getJwtToken()))
//            .andExpect(status().isOk())
//            .andExpect(content().json(expectedResponse));
//  }
}
