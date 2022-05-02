package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.Building;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.exceptions.BuildingDoesNotBelongToPlayerException;
import com.greenfoxacademy.springwebapp.exceptions.BuildingTypeException;
import com.greenfoxacademy.springwebapp.exceptions.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.dtos.KingdomPostDTO;
import com.greenfoxacademy.springwebapp.login.exceptions.InputMissingException;
import com.greenfoxacademy.springwebapp.troop.TroopService;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopsDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import com.greenfoxacademy.springwebapp.utilities.JwtUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@RestController
public class KingdomController {

  private final JwtUtils jwtUtils;

  private final KingdomService kingdomService;

  @Autowired
  private BuildingService buildingService;

  @Autowired
  TroopService troopService;

  private final ModelMapper modelMapper;

  @Autowired
  public KingdomController(KingdomService kingdomService,
                           JwtUtils jwtUtils,
                           @Qualifier("modelMapper") ModelMapper modelMapper) {
    this.kingdomService = kingdomService;
    this.jwtUtils = jwtUtils;
    this.modelMapper = modelMapper;
  }

  @GetMapping("kingdom/troops")
  public TroopsDTO getTroopsByKingdomId(
          @RequestHeader(name = "Authorization", required = false) String jwtToken)
          throws InputMissingException {

    if (jwtToken == null) {
      throw new InputMissingException("JWT token must be present.");
    }

    Integer kingdomId = jwtUtils.getKingdomIdFromJwtToken(jwtToken);

    List<Troop> troopsByKingdom = kingdomService.getTroopsOfKingdom(kingdomId);

    Type listType = new TypeToken<List<TroopDTO>>() {
    }.getType();
    List<TroopDTO> troopDTOs = modelMapper.map(troopsByKingdom, listType);

    return new TroopsDTO(troopDTOs);
  }

  @PostMapping("kingdom/troops")
  public TroopDTO postTroops(@RequestHeader(name = "Authorization", required = false) String jwtToken,
                             @RequestBody(required = false) KingdomPostDTO kingdomPostDTO)
          throws BuildingTypeException,
          BuildingDoesNotBelongToPlayerException,
          NotEnoughResourceException {

    kingdomService.checkInputParameters(kingdomPostDTO, jwtToken);
    Integer kingdomId = jwtUtils.getKingdomIdFromJwtToken(jwtToken);
    Optional<Building> optionalBuilding = buildingService.getBuildingById(kingdomPostDTO.getBuildingId());

    if (optionalBuilding.isPresent()) {
      Building building = optionalBuilding.get();

      kingdomService.checkOwner(building, kingdomId);
      kingdomService.checkBuildingType(building);

      int level = building.getLevel();

      kingdomService.checkResources(building, level);
      return troopService.saveAndGetTroopAsDTO(level, building.getKingdom());
    }
    throw new BuildingDoesNotBelongToPlayerException("There is no building with id", kingdomPostDTO.getBuildingId());
  }
}