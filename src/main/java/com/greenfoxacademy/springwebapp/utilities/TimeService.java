package com.greenfoxacademy.springwebapp.utilities;

import java.time.LocalDateTime;

public interface TimeService {

  LocalDateTime actualTime();

  LocalDateTime timeAtNSecondsLater(long n);

  LocalDateTime timeAtNSecondsAfterTimeStamp(long n, LocalDateTime time);

  boolean timePast(LocalDateTime time);

  long secondsElapsed(LocalDateTime timeStart, LocalDateTime timeEnd);

  long toEpochSecond(LocalDateTime time);

  LocalDateTime toLocalDateTime(long epochSecond);

}
