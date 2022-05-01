package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

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
  public void when_getResource_should_respondResourcesJson() {

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/resources")
        .contentType("application/json")
        .content())
        .andExpect(jsonPath)

  }


}
