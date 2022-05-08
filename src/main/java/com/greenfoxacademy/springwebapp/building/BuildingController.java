package com.greenfoxacademy.springwebapp.building;

import com.greenfoxacademy.springwebapp.building.models.BuildingDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingTypeDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/kingdom/buildings")
public class BuildingController {

  private BuildingService buildingService;

  @Autowired
  public BuildingController(BuildingService buildingService) {
    this.buildingService = buildingService;
  }

  @PostMapping
  public ResponseEntity addBuilding(UsernamePasswordAuthenticationToken user,
                                    @RequestBody BuildingTypeDTO typeDTO) {
    Kingdom kingdom = ((Player) user.getPrincipal()).getKingdom();
    return ResponseEntity.status(201).body(buildingService.addBuilding(typeDTO, kingdom));
  }

  @PutMapping({"/{id}"})
  public ResponseEntity modifyBuildingLevel(Authentication user, @PathVariable(name = "id") Integer buildingId,
                                            @RequestBody BuildingDTO buildingDTO) {
    return ResponseEntity.status(200).body(buildingService.provideDtoAboutBuildingDevResoult(
            buildingDTO, ((Player) user.getPrincipal()).getKingdom(), buildingId));
  }

}
