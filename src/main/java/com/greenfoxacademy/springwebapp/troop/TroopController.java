package com.greenfoxacademy.springwebapp.troop;

<<<<<<< HEAD
import com.greenfoxacademy.springwebapp.exceptions.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
=======
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.troop.models.TroopPostDTO;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
>>>>>>> 1ac9045... feat(put /kingdom/troops/id): implement upgrade troop
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdom/troops")
public class TroopController {

  private TroopService troopService;

  @Autowired
  public TroopController(TroopService troopService) {
    this.troopService = troopService;
  }

  @GetMapping("/{id}")
  public ResponseEntity handleGetTroopById(UsernamePasswordAuthenticationToken user,
                                           @PathVariable int id) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.ok(troopService.fetchTroop(kingdom, id));
  }

  @GetMapping()
  public ResponseEntity<TroopsDTO> getTroopsByKingdomId(Authentication auth) {
    Player player = (Player) auth.getPrincipal();
    TroopsDTO troopsByKingdom = troopService.getTroopsOfKingdom(player.getKingdom());
    return ResponseEntity.ok(troopsByKingdom);
  }

  @PostMapping()
  public ResponseEntity<TroopDTO> postTroops(@RequestBody TroopPostDTO troopPostDTO, Authentication auth)
      throws ForbiddenActionException {
    Player player = (Player) auth.getPrincipal();
    TroopDTO troop = troopService.saveTroop(player.getKingdom(), troopPostDTO);
    return ResponseEntity.status(201).body(troop);
  }

  @PutMapping("/{id}")
  public ResponseEntity upgradeTroop(Principal principal, @PathVariable int id,
                                     @RequestBody TroopPostDTO dto) {
    Player player = (Player) principal;
    return ResponseEntity.ok(troopService.upgradeTroop(player.getKingdom(), id, dto));
  }

}
