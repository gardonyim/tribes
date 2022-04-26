package com.greenfoxacademy.springwebapp.utilities;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class EpochConverter {

  @Value("${time.zone}")
  public static String TIME_ZONE;

  public static long convertToEpochMilli(LocalDateTime ldt) {
    return ldt.atZone(ZoneId.of(TIME_ZONE)).toInstant().toEpochMilli();
  }


}
