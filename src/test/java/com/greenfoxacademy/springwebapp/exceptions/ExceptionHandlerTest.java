package com.greenfoxacademy.springwebapp.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.exceptions.models.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("classpath:data.sql")
@Transactional
public class ExceptionHandlerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  KingdomService kingdomService;

  @Before
  public void setUp() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void when_getKingdom_throw_exception_returnsDefaultErrorMessage()
          throws Exception {
    when(kingdomService.fetchKingdomData(any(Integer.class))).thenThrow(new NullPointerException());

    int reqKingdomId = 999;
    Player existingtestuser =
            new Player(1, "existingtestuser", null, null, null, 0);
    Authentication auth = new UsernamePasswordAuthenticationToken(existingtestuser, null);
    ErrorDTO dto = new ErrorDTO("Unknown error");
    String expectedResponse = mapper.writeValueAsString(dto);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/" + reqKingdomId).principal(auth))
            .andExpect(status().is(500))
            .andExpect(content().json(expectedResponse));
  }

}
