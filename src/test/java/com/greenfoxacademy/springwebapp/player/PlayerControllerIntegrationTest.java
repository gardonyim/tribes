package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.kingdom.KingdomRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Test
  public void givenRegisterURL_postUsername_thenStatusBadRequest_returnsJson()
      throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"\",  "
        + "\"kingdomname\" : \"\" }";
    String jsonResponse = "{ \"status\" :  \"error\", \"message\" : \"Password is required.\" }";
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(jsonResponse));
  }

  @Test
  public void givenRegisterURL_postPassword_thenStatusBadRequest_returnsJson()
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
  public void givenRegisterURL_postEmptyBody_thenStatusBadRequest_returnsJson()
      throws Exception {
    String jsonRequest = "{ \"username\" : \"\",  \"password\" : \"\",  "
        + "\"kingdomname\" : \"\" }";
    String jsonResponse = "{ \"status\" :  \"error\", \"message\" : "
        + "\"Username and password are required.\" }";
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(jsonResponse));
  }

  @Test
  public void givenRegisterURL_postNonUniqueUsername_thenStatusConflict_returnsJson()
      throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"hellothere\", "
        + "\"kingdomname\" : \"The High Ground\" }";
    String jsonResponse = "{ \"status\" :  \"error\", \"message\" : "
        + "\"Username is already taken.\" }";
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
        .contentType("application/json")
        .content(jsonRequest));
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isConflict())
        .andExpect(content().json(jsonResponse));
  }

  @Test
  public void givenRegisterURL_postShortPassword_thenStatusConflict_returnsJson()
      throws Exception {
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"pw\",  "
        + "\"kingdomname\" : \"The High Ground\" }";
    String jsonResponse = "{ \"status\" :  \"error\", \"message\" : "
        + "\"Password must be at least 8 characters.\" }";
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().json(jsonResponse));
  }

  @Test
  public void givenRegisterURL_postUsernamePasswordKingdomname_thenStatusCreated_returnsJson()
      throws Exception {
    int count = kingdomRepository.findAll().size() + 1;
    String jsonRequest = "{ \"username\" : \"obiwan\",  \"password\" : \"hellothere\",  "
        + "\"kingdomname\" : \"The High Ground\" }";
    String jsonResponse = "{ \"id\" : " + count + ",  \"username\" : \"obiwan\",  \"kingdomId\" :"
        + count + " }";
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isCreated())
        .andExpect(content().json(jsonResponse));
  }

  @Test
  public void givenRegisterURL_postUsernamePassword_thenStatusCreated_returnsJson()
      throws Exception {
    int count = kingdomRepository.findAll().size() + 1;
    String jsonRequest = "{ \"username\" : \"luke\",  \"password\" : \"hellothere\",  "
        + "\"kingdomname\" : \"\" }";
    String jsonResponse = "{ \"id\" : " + count + ",  \"username\" : \"luke\",  \"kingdomId\" :"
        + count + " }";
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType("application/json")
            .content(jsonRequest))
        .andExpect(status().isCreated())
        .andExpect(content().json(jsonResponse));
    Assert.assertEquals("luke's kingdom", kingdomRepository.findById(count).get().getName());
  }
}
