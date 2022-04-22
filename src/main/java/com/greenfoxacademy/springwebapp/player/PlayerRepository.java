package com.greenfoxacademy.springwebapp.player;

import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

<<<<<<< HEAD
  Optional<Player> findFirstByUsername(String username);

=======
  Optional<Player> findByUsername(String username);
>>>>>>> 3a3786b (refactor(Security): Change PlayerTokenDTO to Player in the SecurityContext)
}
