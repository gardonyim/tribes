package com.greenfoxacademy.springwebapp.resource.models;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ResourceTypeConverter implements AttributeConverter<ResourceType, String> {


  @Override
  public String convertToDatabaseColumn(ResourceType resourceType) {
    if (resourceType == null ) {
      return null;
    }
    return resourceType.getDescription();
  }

  @Override
  public ResourceType convertToEntityAttribute(String description) {
    if (description == null) {
      return null;
    }

    return Stream.of(ResourceType.values())
        .filter(rt -> rt.getDescription().equals(description))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
