package com.greenfoxacademy.springwebapp.login;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql("classpath:data.sql")
@Transactional
public class LoginControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void when_postLoginWithoutUsername_should_respondBadRequestStatusAndProperJson() throws Exception {
    String jsonRequest = "{ \"password\" : \"burgonya\"}";
    String expectedResponse = "{  \"status\": \"error\", \"message\": \"Username is required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithoutPassword_should_respondBadRequestStatusAndProperJson() throws Exception {
    String jsonRequest = "{ \"username\" : \"krumpli\"}";
    String expectedResponse = "{  \"status\": \"error\", \"message\": \"Password is required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithoutUsernameAndPassword_should_respondBadRequestStatusAndProperJson() throws Exception {
    String jsonRequest = "{ }";
    String expectedResponse = "{  \"status\": \"error\", \"message\": \"All fields are required.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithInvalidUsernameOrPassword_should_respond401StatusAndProperJson() throws Exception {
    String jsonRequest = "{ \"username\" : \"existingtestuser\",  \"password\" : \"rosszjelszo\"}";
    String expectedResponse = "{  \"status\": \"error\", \"message\": \"Username or password is incorrect.\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(status().is(401))
            .andExpect(content().json(expectedResponse));
  }

  @Test
  public void when_postLoginWithValidUsernameAndPassword_should_respondOkStatusAndJwtToken() throws Exception {
    String jsonRequest = "{ \"username\" : \"existingtestuser\",  \"password\" : \"password\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
                    .contentType("application/json")
                    .content(jsonRequest))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", Matchers.stringContainsInOrder(".", ".")))
            .andExpect(jsonPath("$.status").value("ok"));
  }
}

