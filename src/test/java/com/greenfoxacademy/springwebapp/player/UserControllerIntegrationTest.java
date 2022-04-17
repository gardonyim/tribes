package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

  private PlayerService playerService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void givenRegisterURL_postUsernamePasswordKingdomname_thenStatusCreated_returnsJSON()
      throws Exception {

  }


}
