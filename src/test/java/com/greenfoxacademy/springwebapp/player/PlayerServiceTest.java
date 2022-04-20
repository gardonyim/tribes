package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.NoPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.NoUsernameAndPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.NoUsernameException;
import com.greenfoxacademy.springwebapp.exceptions.ShortPasswordException;
import com.greenfoxacademy.springwebapp.exceptions.UsernameAlreadyExistsException;
import com.greenfoxacademy.springwebapp.kingdom.KingdomServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

  @Mock
  PlayerRepository playerRepository;

  @Mock
  KingdomServiceImpl kingdomService;

  @Mock
  PasswordEncoder passwordEncoder;

  @InjectMocks
  PlayerServiceImp playerService;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void when_save_player_without_password_it_should_throw_exception() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");

    exceptionRule.expect(NoPasswordException.class);
    exceptionRule.expectMessage("Password is required.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_save_player_without_username_it_should_throw_exception() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setPassword("testpassword");

    exceptionRule.expect(NoUsernameException.class);
    exceptionRule.expectMessage("Username is required.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_save_player_without_username_and_password_it_should_throw_exception() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();

    exceptionRule.expect(NoUsernameAndPasswordException.class);
    exceptionRule.expectMessage("Username and password are required.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_save_player_nonUniqueUsername_it_should_throw_exception() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");
    reqDTO.setPassword("testpassword");

    Optional<Player> optionalPlayer = Optional.of(new Player());
    when(playerRepository.findByUsername(any())).thenReturn(optionalPlayer);

    exceptionRule.expect(UsernameAlreadyExistsException.class);
    exceptionRule.expectMessage("Username is already taken.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_save_player_shortPassword_it_should_throw_exception() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");
    reqDTO.setPassword("testpw");

    Optional<Player> optionalPlayer = Optional.empty();
    when(playerRepository.findByUsername(any())).thenReturn(optionalPlayer);

    exceptionRule.expect(ShortPasswordException.class);
    exceptionRule.expectMessage("Password must be at least 8 characters.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_save_player_it_should_return_registrationResDTO() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");
    reqDTO.setPassword("password");
    Kingdom testkingdom = new Kingdom();
    testkingdom.setId(999);

    when(playerRepository.save(any(Player.class))).then(returnsFirstArg());
    when(kingdomService.save(any(), any())).thenReturn(testkingdom);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");

    Assert.assertEquals(reqDTO.getUsername(), playerService.savePlayer(reqDTO).getUsername());
    Assert.assertEquals(testkingdom.getId(), playerService.savePlayer(reqDTO).getKingdomId());
  }
}
