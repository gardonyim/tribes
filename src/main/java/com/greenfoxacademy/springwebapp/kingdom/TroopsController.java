package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.WrongIdException;
import com.greenfoxacademy.springwebapp.kingdom.dtos.KingdomPostDTO;
import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.troop.TroopService;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopsDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.utilities.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TroopsController {

  private final JwtUtils jwtUtils;

  private final KingdomService kingdomService;

  @Autowired
  private BuildingService buildingService;

  @Autowired
  TroopService troopService;

  private final ModelMapper modelMapper;

  @Autowired
  public TroopsController(KingdomService kingdomService,
                          JwtUtils jwtUtils,
                          @Qualifier("modelMapper") ModelMapper modelMapper) {
    this.kingdomService = kingdomService;
    this.jwtUtils = jwtUtils;
    this.modelMapper = modelMapper;
  }

  @GetMapping("kingdom/troops")
  public ResponseEntity<TroopsDTO> getTroopsByKingdomId() {

    Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Integer kingdomId = player.getKingdom().getId();

    List<Troop> troopsByKingdom = troopService.getTroopsOfKingdom(kingdomId);

    List<TroopDTO> troopDTOs = troopService.mapTroopsToTroopDTO(troopsByKingdom);

    return ResponseEntity.ok(new TroopsDTO(troopDTOs));
  }

  @PostMapping("kingdom/troops")
  public ResponseEntity<TroopDTO> postTroops(@RequestBody KingdomPostDTO kingdomPostDTO)
          throws WrongIdException {

    Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Integer kingdomId = player.getKingdom().getId();
    Integer buildingId = kingdomPostDTO.getBuildingId();

    kingdomService.checkInputParameters(kingdomPostDTO);
    Optional<Building> optionalBuilding = buildingService.getBuildingById(buildingId);

    if (optionalBuilding.isPresent()) {
      Building building = optionalBuilding.get();

      kingdomService.checkOwner(building, kingdomId);
      kingdomService.checkBuildingType(building);

      int level = building.getLevel();

      kingdomService.checkResources(building, level);
      return troopService.saveAndGetTroopAsDTO(level, building.getKingdom());
    }
    throw new WrongIdException("There is no building with this id");
  }
}