package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.Resource;
import com.greenfoxacademy.springwebapp.resource.models.ResourceType;
import com.greenfoxacademy.springwebapp.utilities.TimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
@Transactional
@Sql("classpath:data.sql")
public class ResourceControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ResourceRepository resourceRepository;

  @Test
  public void when_getResource_should_respondResourcesJson() throws Exception {
    LocalDateTime ldt = LocalDateTime.now();
    Resource resource1 = new Resource();
    resource1.setResourceType(ResourceType.FOOD);
    resource1.setAmount(100);
    resource1.setGeneration(10);
    resource1.setUpdatedAt(ldt);
    Resource resource2 = new Resource();
    resource2.setResourceType(ResourceType.GOLD);
    resource2.setAmount(1000);
    resource2.setGeneration(100);
    resource2.setUpdatedAt(ldt);

    Kingdom myKingdom = new Kingdom();
    myKingdom.setResources(new ArrayList<>(Arrays.asList(resource1, resource2)));
    Player myPlayer = new Player();
    myPlayer.setKingdom(myKingdom);
    Authentication auth = new UsernamePasswordAuthenticationToken(myPlayer, null);
    long ldtEpochInSec = TimeService.toEpochSecond(ldt);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/resources")
            .principal(auth))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.resources").exists())
        .andExpect(jsonPath("$.resources.length()").value(2))
        .andExpect(jsonPath("$.resources[0].type").value("food"))
        .andExpect(jsonPath("$.resources[0].amount").value(100))
        .andExpect(jsonPath("$.resources[0].generation").value(10))
        .andExpect(jsonPath("$.resources[0].updatedAt").value(ldtEpochInSec))
        .andExpect(jsonPath("$.resources[1].type").value("gold"))
        .andExpect(jsonPath("$.resources[1].amount").value(1000))
        .andExpect(jsonPath("$.resources[1].generation").value(100))
        .andExpect(jsonPath("$.resources[1].updatedAt").value(ldtEpochInSec))
    ;

  }


}
