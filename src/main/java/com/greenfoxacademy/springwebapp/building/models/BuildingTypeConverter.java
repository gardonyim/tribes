package com.greenfoxacademy.springwebapp.building.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BuildingTypeConverter implements AttributeConverter<BuildingType, String> {

  @Override
  public String convertToDatabaseColumn(BuildingType buildingType) {
    if (buildingType == null) {
      return null;
    }
    return buildingType.getName();
  }

  @Override
  public BuildingType convertToEntityAttribute(String name) {
    if (name == null) {
      return null;
    }

    return Stream.of(BuildingType.values())
        .filter(b -> b.getName().equals(name))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}