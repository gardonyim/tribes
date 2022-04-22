package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.exceptions.RequestParameterMissingException;
import com.greenfoxacademy.springwebapp.exceptions.RequestNotAcceptableException;
import com.greenfoxacademy.springwebapp.exceptions.RequestCauseConflictException;
import com.greenfoxacademy.springwebapp.kingdom.KingdomServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.player.models.RegistrationReqDTO;
import com.greenfoxacademy.springwebapp.player.models.RegistrationResDTO;
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
  PlayerServiceImpl playerService;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void when_savePlayerWithoutPassword_should_throwException() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");

    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("Password is required.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_savePlayerWithoutUsername_should_throwException() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setPassword("testpassword");

    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("Username is required.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_savePlayerWithoutUsernameAndPassword_should_throwException() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();

    exceptionRule.expect(RequestParameterMissingException.class);
    exceptionRule.expectMessage("Username and password are required.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_savePlayerWithNonUniqueUsername_should_throwException() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");
    reqDTO.setPassword("testpassword");

    Optional<Player> optionalPlayer = Optional.of(new Player());
    when(playerRepository.findFirstByUsername(any())).thenReturn(optionalPlayer);

    exceptionRule.expect(RequestCauseConflictException.class);
    exceptionRule.expectMessage("Username is already taken.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_savePlayerWithShortPassword_should_throwException() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");
    reqDTO.setPassword("testpw");

    exceptionRule.expect(RequestNotAcceptableException.class);
    exceptionRule.expectMessage("Password must be at least 8 characters.");
    playerService.savePlayer(reqDTO);
  }

  @Test
  public void when_savePlayer_should_returnRegistrationResDTO() {
    RegistrationReqDTO reqDTO = new RegistrationReqDTO();
    reqDTO.setUsername("testuser");
    reqDTO.setPassword("password");
    Kingdom testKingdom = new Kingdom();
    testKingdom.setId(999);
    Player testPlayer = new Player(reqDTO.getUsername(), reqDTO.getPassword(), testKingdom, "", 0);
    testPlayer.setId(999);

    when(playerRepository.save(any(Player.class))).thenReturn(testPlayer);
    when(kingdomService.save(any(), any())).thenReturn(testKingdom);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");

    RegistrationResDTO result = playerService.savePlayer(reqDTO);

    Assert.assertEquals(testPlayer.getId(), result.getId());
    Assert.assertEquals(reqDTO.getUsername(), result.getUsername());
    Assert.assertEquals(testKingdom.getId(), result.getKingdomId());
  }
}
