package com.greenfoxacademy.springwebapp.building;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingsDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kingdom/buildings")
public class BuildingController {

  private BuildingService buildingService;

  @Autowired
  public BuildingController(BuildingService buildingService) {
    this.buildingService = buildingService;
  }

  @GetMapping
  public ResponseEntity<BuildingsDTO> getBuildingsDTO(Authentication user) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.status(200).body(buildingService.getBuildingDtoList(kingdom));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BuildingDTO> getBuildingDTO(Authentication user, @PathVariable Integer id) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.status(200).body(buildingService.getBuildingDTO(id, kingdom));
  }

  @PostMapping
  public ResponseEntity addBuilding(UsernamePasswordAuthenticationToken user, @RequestBody BuildingTypeDTO typeDTO) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.status(201).body(buildingService.addBuilding(typeDTO, kingdom));
  }

}
