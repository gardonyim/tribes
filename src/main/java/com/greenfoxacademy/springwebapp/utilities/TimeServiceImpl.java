package com.greenfoxacademy.springwebapp.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class TimeServiceImpl implements TimeService {

  @Override
  public LocalDateTime actualTime() {
    return LocalDateTime.now(ZoneId.of("UTC"));
  }

  @Override
  public LocalDateTime timeAtNSecondsLater(long n) {
    return timeAtNSecondsAfterTimeStamp(n, actualTime());
  }

  @Override
  public LocalDateTime timeAtNSecondsAfterTimeStamp(long n, LocalDateTime time) {
    return time.plusSeconds(n);
  }

  @Override
  public boolean timePast(LocalDateTime time) {
    return time.isBefore(actualTime());
  }

  @Override
  public long secondsElapsed(LocalDateTime timeStart, LocalDateTime timeEnd) {
    return ChronoUnit.SECONDS.between(timeStart, timeEnd);
  }

  @Override
  public long toEpochSecond(LocalDateTime time) {
    return time.toEpochSecond(ZoneOffset.UTC);
  }

  @Override
  public LocalDateTime toLocalDateTime(long epochSecond) {
//    return Instant.ofEpochSecond(epochSecond).atZone(ZoneId.systemDefault()).toLocalDateTime();
    return LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
  }
}
