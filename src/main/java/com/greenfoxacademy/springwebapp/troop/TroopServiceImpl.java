package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.kingdom.models.Kingdom;
import com.greenfoxacademy.springwebapp.troop.dtos.TroopDTO;
import com.greenfoxacademy.springwebapp.troop.models.Troop;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TroopServiceImpl implements TroopService {

  private final TroopRepository troopRepository;

  @Autowired
  @Qualifier("modelMapper")
  private ModelMapper modelMapper;

  @Autowired
  public TroopServiceImpl(TroopRepository troopRepository) {
    this.troopRepository = troopRepository;
  }

  @Override
  public Troop addNewTroop(Troop troop) {
    return troopRepository.save(troop);
  }

  @Override
  public ResponseEntity<TroopDTO> saveAndGetTroopAsDTO(int level, Kingdom kingdom) {
    Troop troop = new Troop();
    troop.setLevel(level);
    troop.setHp(level * 20);
    troop.setKingdom(kingdom);
    troop.setAttack(level * 10);
    troop.setDefence(level * 5);
    troop.setStartedAt(LocalDateTime.now());
    troop.setFinishedAt(LocalDateTime.now().plus(level * 30L, ChronoUnit.SECONDS));
    return ResponseEntity.ok(modelMapper.map(addNewTroop(troop), TroopDTO.class));
  }

  @Override
  public List<Troop> getTroopsOfKingdom(Integer kingdomId) {
    return troopRepository.findTroopsByKingdomId(kingdomId);
  }

  @Override
  public List<TroopDTO> mapTroopsToTroopDTO(List<Troop> troopsByKingdom) {
    Type listType = new TypeToken<List<TroopDTO>>() {}.getType();
    List<TroopDTO> troopDTOs = modelMapper.map(troopsByKingdom, listType);
    return troopDTOs;
  }
}
