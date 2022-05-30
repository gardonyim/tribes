package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

  Optional<Player> findFirstByUsername(String username);

  Optional<Player> findPlayerByUsernameAndPassword(String username, String password);

  @Query(value = "SELECT * FROM players "
      + "JOIN kingdoms k on players.id = k.player_id "
      + "JOIN locations l on l.id = k.location_id "
      + "WHERE x_coordinate BETWEEN :xMin AND :xMax "
      + "AND y_coordinate BETWEEN :yMin AND :yMax",
      nativeQuery = true)
  List<Player> findAllNearBy(@Param("xMin") int xmin, @Param("xMax") int xmax,
                             @Param("yMin") int ymin, @Param("yMax") int ymax);
  
}
