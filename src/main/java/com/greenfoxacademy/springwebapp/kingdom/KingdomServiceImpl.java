package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.location.LocationService;
import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;
  private LocationService locationService;

  @Autowired
  public KingdomServiceImpl(
      KingdomRepository kingdomRepository, LocationService locationService) {
    this.kingdomRepository = kingdomRepository;
    this.locationService = locationService;
  }

  @Override
  public Kingdom save(String kingdomName, Player player) {
    if (kingdomName == null || kingdomName.isEmpty()) {
      kingdomName = player.getUsername() + "'s kingdom";
    }
    Kingdom kingdom = kingdomRepository.save(new Kingdom(kingdomName, player,
        locationService.createLocation()));
    return kingdom;
  }

}
