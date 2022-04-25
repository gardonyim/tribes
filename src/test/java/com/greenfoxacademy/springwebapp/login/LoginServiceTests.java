package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTests {

  @InjectMocks
  LoginServiceImpl loginService;

  @Mock
  PlayerRepository playerRepository;

  @Test
  public void authenticateTest_returnPlayer() {
    when(playerRepository.findPlayerByUsernameAndPassword(any(), any())).thenReturn(Optional.of(new Player()));
    Assertions.assertNotNull(loginService.authenticate("krumpli", "krumpli"));
  }

  @Test
  public void authenticateTest_returnNull() {
    when(playerRepository.findPlayerByUsernameAndPassword(any(), any())).thenReturn(null);
    Assertions.assertNull(loginService.authenticate("krumpli", "krumpli"));
  }
}
