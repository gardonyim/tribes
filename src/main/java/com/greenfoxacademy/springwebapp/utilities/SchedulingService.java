package com.greenfoxacademy.springwebapp.utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulingService {

  public static void scheduler(Runnable runnable, long delay) {
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.schedule(runnable, delay, TimeUnit.SECONDS);
  }

}
