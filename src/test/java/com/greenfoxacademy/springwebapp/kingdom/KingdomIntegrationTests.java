package com.greenfoxacademy.springwebapp.kingdom;

import com.google.gson.Gson;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.exceptions.ErrorDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.player.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
public class KingdomIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  PlayerService playerService;

  @Value("classpath:CH4-12/oneTroopResponse.json")
  Resource oneTroopResponse;

  Gson gson = new Gson();

  //
  // POST
  //

//  @Sql({"classpath:data.sql", "classpath:building.sql", "classpath:resource.sql"})
//  @Test
//  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//  public void when_postKingdomTroops_should_respondOkResponseStatusWithTroop()
//          throws Exception {
//    TroopPostDTO troopPostDTO = new TroopPostDTO();
//    troopPostDTO.setBuildingId(1);
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(gson.toJson(troopPostDTO))
//                    .header("Authorization", "Bearer " + getJwtToken()))
//            .andExpect(status().is(200))
//            .andExpect(jsonPath("$.id").value(1))
//            .andExpect(jsonPath("$.level").value(1))
//            .andExpect(jsonPath("$.hp").value(20))
//            .andExpect(jsonPath("$.attack").value(10))
//            .andExpect(jsonPath("$.defence").value(5));
//  }
//
//  @Sql({"classpath:data.sql", "classpath:building.sql", "classpath:resource.sql"})
//  @Test
//  public void when_postKingdomTroops_should_respond400ResponseStatusWithBuildingDoesNotBelongToPlayerException()
//          throws Exception {
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .header("Authorization", "Bearer " + getJwtToken()))
//            .andExpect(status().is(400))
//            .andExpect(content().json(
//                    gson.toJson(
//                            new ErrorDTO("buildingId must be present"))));
//  }
//
//  @Sql({"classpath:data.sql", "classpath:resource.sql"})
//  @Test
//  public void when_postKingdomTroops_should_respond403ResponseStatusWithBuildingNotFound()
//          throws Exception {
//    TroopPostDTO troopPostDTO = new TroopPostDTO();
//    troopPostDTO.setBuildingId(1);
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(gson.toJson(troopPostDTO))
//                    .header("Authorization", "Bearer " + getJwtToken()))
//            .andExpect(status().is(403))
//            .andExpect(content().json(
//                    gson.toJson(
//                            new ErrorDTO("There is no building with this id")
//                    )
//            ));
//  }
//
//  @Sql({"classpath:data.sql", "classpath:building_wrong_type.sql", "classpath:resource.sql"})
//  @Test
//  public void when_postKingdomTroops_should_respond406ResponseStatusWithBuildingWrongType()
//          throws Exception {
//    TroopPostDTO troopPostDTO = new TroopPostDTO();
//    troopPostDTO.setBuildingId(1);
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(gson.toJson(troopPostDTO))
//                    .header("Authorization", "Bearer " + getJwtToken()))
//            .andExpect(status().is(406))
//            .andExpect(content().json(
//                    gson.toJson(
//                            new ErrorDTO("Not a valid academy id"))));
//  }
//
//  @Sql({"classpath:data.sql", "classpath:building.sql"})
//  @Test
//  public void when_postKingdomTroops_should_respond409ResponseStatusWithNotEnoughResource()
//          throws Exception {
//    TroopPostDTO troopPostDTO = new TroopPostDTO();
//    troopPostDTO.setBuildingId(1);
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(gson.toJson(troopPostDTO))
//                    .header("Authorization", "Bearer " + getJwtToken()))
//            .andExpect(status().is(409))
//            .andExpect(content().json(
//                    gson.toJson(
//                            new ErrorDTO("Not enough resource"))));
//  }

}