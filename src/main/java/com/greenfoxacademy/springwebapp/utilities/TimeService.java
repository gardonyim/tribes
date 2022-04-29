package com.greenfoxacademy.springwebapp.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class TimeService {

  public static LocalDateTime actualTime() {
    return LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
  }

  public static LocalDateTime timeAtNSecondsLater(long n) {
    return timeAtNSecondsAfterTimeStamp(n, actualTime());
  }

  public static LocalDateTime timeAtNSecondsAfterTimeStamp(long n, LocalDateTime time) {
    return time.plusSeconds(n);
  }

  public static boolean timePast(LocalDateTime time) {
    return time.isBefore(actualTime());
  }

  public static long secondsElapsed(LocalDateTime timeStart, LocalDateTime timeEnd) {
    return ChronoUnit.SECONDS.between(timeStart, timeEnd);
  }

  public static long toEpochSecond(LocalDateTime time) {
    return time.toEpochSecond(ZoneOffset.UTC);
  }

  public static LocalDateTime toLocalDateTime(long epochSecond) {
    return LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
  }
}
