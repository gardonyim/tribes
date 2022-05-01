package com.greenfoxacademy.springwebapp.utilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeServiceTest {

  private TimeService timeService;

  @Before
  public void init() {
    timeService = new TimeService();
  }

  @Test
  public void actualTime_ValidExpectedValue_Equals() {
    Assert.assertTrue(now().equals(timeService.actualTime()));
  }

  @Test
  public void timeAtNSecondsLater_ValidExpectedValue_Equals() {
    Assert.assertEquals(6L, ChronoUnit.SECONDS.between(now(), timeService.timeAtNSecondsLater(6)));
  }

  @Test
  public void timeAtNSecondsAfterTimeStamp_ValidExpectedValue_Equals() {
    Assert.assertEquals(6L, ChronoUnit.SECONDS.between(now(),
            timeService.timeAtNSecondsAfterTimeStamp(6, now())));
  }

  @Test
  public void timePast_ValidExpectedValue_True() {
    Assert.assertTrue(timeService.timePast(now().minusSeconds(6L)));
  }

  @Test
  public void timePast_InvalidExpectedValue_False() {
    Assert.assertFalse(timeService.timePast(now().plusSeconds(6L)));
  }

  @Test
  public void secondsElapsed_ValidExpectedValue_Equals() {
    Assert.assertEquals(6L, timeService.secondsElapsed(LocalDateTime.now(),
        LocalDateTime.now().plusSeconds(6)));
  }

  @Test
  public void toEpochSecond_ValidExpectedValue_Equals() {
    Assert.assertEquals(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        timeService.toEpochSecond(LocalDateTime.now()));
  }

  @Test
  public void toLocalDateTime_ValidExpectedValue_Equals() {
    long epochTime = Instant.now().getEpochSecond();
    Assert.assertEquals(LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC),
        timeService.toLocalDateTime(epochTime));
  }

  private LocalDateTime now() {
    return LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
  }

}
