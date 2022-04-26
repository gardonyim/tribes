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

  private TimeServiceImpl timeService;

  @Before
  public void init() {
    timeService = new TimeServiceImpl();
  }

  @Test
  public void actualTime_ValidExpectedValue_Equals() {
    Assert.assertTrue(LocalDateTime.now(ZoneId.of("UTC")).equals(timeService.actualTime()));
  }

  @Test
  public void timeAtNSecondsLater_ValidExpectedValue_Equals() {
    Assert.assertEquals(6L, ChronoUnit.SECONDS.between(LocalDateTime.now(ZoneId.of("UTC")),
        timeService.timeAtNSecondsLater(6)));
  }

  @Test
  public void timeAtNSecondsAfterTimeStamp_ValidExpectedValue_Equals() {
    Assert.assertEquals(6L, ChronoUnit.SECONDS.between(LocalDateTime.now(ZoneId.of("UTC")),
        timeService.timeAtNSecondsAfterTimeStamp(6, LocalDateTime.now(ZoneId.of("UTC")))));
  }

  @Test
  public void timePast_ValidExpectedValue_True() {
    Assert.assertTrue(timeService.timePast(LocalDateTime.now(ZoneId.of("UTC")).minusSeconds(6L)));
  }

  @Test
  public void timePast_InvalidExpectedValue_False() {
    Assert.assertFalse(timeService.timePast(LocalDateTime.now(ZoneId.of("UTC")).plusSeconds(6L)));
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

}
