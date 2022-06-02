package com.greenfoxacademy.springwebapp.battle;

import com.greenfoxacademy.springwebapp.battle.models.BattleDetails;
import com.greenfoxacademy.springwebapp.battle.models.BattleReqDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.KingdomServiceImpl;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.LocationServiceImpl;
import com.greenfoxacademy.springwebapp.location.models.Location;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.greenfoxacademy.TestUtils.buildingBuilder;
import static com.greenfoxacademy.TestUtils.defaultKingdom;
import static com.greenfoxacademy.TestUtils.defaultTroop;
import static com.greenfoxacademy.TestUtils.kingdomBuilder;
import static com.greenfoxacademy.TestUtils.troopBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BattleServiceTest {

  @Mock
  KingdomServiceImpl kingdomService;

  @Mock
  LocationServiceImpl locationService;

  @InjectMocks
  BattleServiceImpl battleService;

  @Test
  public void when_prepareBattleDetails_should_returnValidBattleDetails() {
    Kingdom defender = kingdomBuilder()
        .withTroops(Arrays.asList(troopBuilder().withId(1).build(), troopBuilder().withId(2).build()))
        .withBuildings(Collections.singletonList(buildingBuilder(BuildingType.TOWNHALL).withLevel(1).build()))
        .build();
    when(kingdomService.findById(anyInt())).thenReturn(defender);
    when(locationService.findShortestPath(any(), any())).thenReturn(Arrays.asList(new Location(), new Location()));
    Troop troop3 = troopBuilder().withId(3).build();
    Troop troop4 = troopBuilder().withId(4).build();
    Troop troop5 = troopBuilder().withId(5).build();
    Kingdom attacker = kingdomBuilder().withTroops(Arrays.asList(troop3, troop4, troop5)).build();
    BattleReqDTO reqDTO = new BattleReqDTO();
    reqDTO.setTroopIds(Arrays.asList(3, 4));
    BattleDetails expected =
        new BattleDetails(attacker, defender, Arrays.asList(troop3, troop4), defender.getTroops(), 1, 1);

    BattleDetails actual = battleService.prepareBattleDetails(attacker, 1, reqDTO);

    Assert.assertEquals(expected, actual);
  }


  @Test
  public void when_battleExecution_should_returnBattleDetailsContainingTroopsWithReducedHp() {
    Troop attacker = troopBuilder().withDefence(9).build();
    List<Troop> attackers = Collections.singletonList(attacker);
    Troop defender = defaultTroop();
    List<Troop> defenders = Collections.singletonList(defender);
    BattleDetails battleDetails = new BattleDetails(defaultKingdom(), defaultKingdom(), attackers, defenders, 0, 0);

    battleDetails = battleService.battleExecution(battleDetails);

    Assert.assertEquals((Integer) 17, attacker.getHp());
    Assert.assertEquals((Integer) 5, defender.getHp());
    Assert.assertTrue(battleDetails.getAttackerClones().contains(attacker));
    Assert.assertTrue(battleDetails.getDefenderClones().contains(defender));
    Assert.assertTrue(battleDetails.getFallenTroops().isEmpty());
  }

  @Test
  public void when_battleExecutionWithDifferentNumberOfTroops_should_allTroopsParticipateAndReturnValidBattleDetails() {
    Troop attacker = troopBuilder().withDefence(9).build();
    List<Troop> attackers = Collections.singletonList(attacker);
    Troop defender1 = defaultTroop();
    Troop defender2 = defaultTroop();
    List<Troop> defenders = Arrays.asList(defender1, defender2);
    BattleDetails battleDetails = new BattleDetails(defaultKingdom(), defaultKingdom(), attackers, defenders, 0, 0);

    battleDetails = battleService.battleExecution(battleDetails);

    Assert.assertEquals((Integer) 14, attacker.getHp());
    Assert.assertEquals((Integer) 5, defender1.getHp());
    Assert.assertEquals((Integer) 5, defender2.getHp());
    Assert.assertTrue(battleDetails.getAttackerClones().contains(attacker));
    Assert.assertTrue(battleDetails.getDefenderClones().containsAll(Arrays.asList(defender1, defender2)));
    Assert.assertTrue(battleDetails.getFallenTroops().isEmpty());
  }

  @Test
  public void when_battleExecution_should_removeFallenTroopsAndReturnValidBattleDetails() {
    Troop attacker = troopBuilder().withDefence(9).build();
    List<Troop> attackers = Collections.singletonList(attacker);
    Troop defender1 = defaultTroop();
    Troop defender2 = troopBuilder().withHP(10).build();
    List<Troop> defenders = new ArrayList<>();
    defenders.add(defender1);
    defenders.add(defender2);
    BattleDetails battleDetails = new BattleDetails(defaultKingdom(), defaultKingdom(), attackers, defenders, 0, 0);

    battleDetails = battleService.battleExecution(battleDetails);

    Assert.assertEquals((Integer) 16, attacker.getHp());
    Assert.assertEquals((Integer) 5, defender1.getHp());
    Assert.assertEquals((Integer) 0, defender2.getHp());
    Assert.assertTrue(battleDetails.getFallenTroops().contains(defender2));
    Assert.assertFalse(battleDetails.getDefenderClones().contains(defender2));
  }

}
