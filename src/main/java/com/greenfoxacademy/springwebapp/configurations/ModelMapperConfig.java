package com.greenfoxacademy.springwebapp.configurations;

import com.greenfoxacademy.springwebapp.troop.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean("modelMapper")
  public ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    TypeMap<Troop, TroopDTO> propertyMapper = modelMapper.createTypeMap(Troop.class, TroopDTO.class);
    propertyMapper.addMapping(Troop::getStartedAtAsLong, TroopDTO::setStartedAt);
    propertyMapper.addMapping(Troop::getFinishedAtAsLong, TroopDTO::setFinishedAt);

    return modelMapper;
  }
}