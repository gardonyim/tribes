package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerIntegrationTests {

  @MockBean
  LoginServiceImpl loginService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void when_postLoginWithoutUsername_should_respondBadRequestStatusAndProperJson() throws Exception {
    String jsonRequest = "{ \"password\" : \"krumpli\"}";
    String expectedResponse = "{  \"error\" : \"Username is required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithoutPassword_should_respondBadRequestStatusAndProperJson() throws Exception {
    String jsonRequest = "{ \"username\" : \"krumpli\"}";
    String expectedResponse = "{ \"error\" : \"Password is required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithoutUsernameAndPassword_should_respondBadRequestStatusAndProperJson() throws Exception {
    String jsonRequest = "{ }";
    String expectedResponse = "{ \"error\" : \"All fields are required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWitInvalidUsernameOrPassword_should_respond401StatusAndProperJson() throws Exception {
    when(loginService.authenticate(any(), any())).thenReturn(null);

    String jsonRequest = "{ \"username\" : \"krumpli\",  \"password\" : \"\"}";
    String expectedResponse = "{\"error\" : \"Username or password is incorrect.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().is(401))
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithValidUsernameAndPassword_should_respondOkStatusAndJwtToken() throws Exception {
    Player player = new Player();
    player.setUsername("krumpli");
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1);
    kingdom.setName("krumpli");
    player.setKingdom(kingdom);
    when(loginService.authenticate(any(), any())).thenReturn(player);

    String jsonRequest = "{ \"username\" : \"krumpli\",  \"password\" : \"\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isOk());
  }
}
