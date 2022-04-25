package com.greenfoxacademy.springwebapp.utilities;

import java.time.LocalDateTime;

public interface TimeService {

  LocalDateTime actualTime();

  LocalDateTime timeAtNSecondsLater(int n);

  LocalDateTime timeAtNSecondsAfterTimeStamp(int n, LocalDateTime time);

  boolean timePast(LocalDateTime time);

  int secondsElapsed(LocalDateTime timeStart, LocalDateTime timeEnd);
}
