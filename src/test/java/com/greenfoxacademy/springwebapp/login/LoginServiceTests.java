package com.greenfoxacademy.springwebapp.login;

import com.greenfoxacademy.springwebapp.login.dtos.LoginDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.login.exceptions.InputWrongException;
import com.greenfoxacademy.springwebapp.player.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
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

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void authenticateTest_returnPlayer() {
    Optional<Player> expected = Optional.of(new Player());
    when(playerRepository.findFirstByUsername(any())).thenReturn(expected);

    Optional<Player> actual = loginService.findPlayerByName("krumpli");

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void authenticateTest_returnNull() {
    Optional<Player> expected = Optional.empty();
    when(playerRepository.findFirstByUsername("krumpli")).thenReturn(expected);

    Optional<Player> actual = loginService.findPlayerByName("krumpli");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void when_authenticateWithWrongUsernameOrPassword_should_throwException()
          throws InputMissingException, InputWrongException {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setUsername("krumpli");
    loginDTO.setPassword("krumpli2");

    exceptionRule.expect(InputWrongException.class);
    exceptionRule.expectMessage("Username or password is incorrect.");
    loginService.authenticateWithLoginDTO(loginDTO);
  }
}

