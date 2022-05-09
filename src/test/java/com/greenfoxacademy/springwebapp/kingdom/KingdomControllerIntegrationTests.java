package com.greenfoxacademy.springwebapp.kingdom;

import com.google.gson.Gson;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomPutDTO;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
public class KingdomControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  private final Gson gson = new Gson();

  @Test
  public void when_putKingdom_emptyName_respondWith400() throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
            new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);

    mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/kingdom")
                    .principal(auth)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(new KingdomPutDTO())))
            .andExpect(status().is(400))
            .andExpect(content().json(gson.toJson(new ErrorDTO("name is required."))));
  }

  @Test
  public void when_putKingdom_withName_respondWith200() throws Exception {
    Kingdom existingkingdom = new Kingdom(1, new Location());
    Player existingtestuser =
            new Player(1, "existingtestuser", null, existingkingdom, null, 0);
    existingkingdom.setPlayer(existingtestuser);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    KingdomPutDTO kingdomPutDTO = new KingdomPutDTO();
    kingdomPutDTO.setName("this is some name");

    String expectedResponse = "{\"id\":1, \"name\":\"this is some name\",\"userId\":1,\"buildings\":[],"
            + "\"resources\":[], \"troops\":[], \"location\": {\"xcoordinate\":0,\"ycoordinate\":0}}";

    mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/kingdom")
                    .principal(auth)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(kingdomPutDTO)))
            .andExpect(status().is(200))
            .andExpect(content().json(expectedResponse));
  }

}
