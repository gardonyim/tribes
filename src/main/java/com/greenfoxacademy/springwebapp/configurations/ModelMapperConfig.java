package com.greenfoxacademy.springwebapp.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean("modelMapper")
  public ModelMapper getModelMapper() {
    return new ModelMapper();
  }
}
