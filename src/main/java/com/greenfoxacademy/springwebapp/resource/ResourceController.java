package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.player.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

  private final ResourceService resourceService;

  @Autowired
  public ResourceController(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @GetMapping("/kingdom/resources")
  public ResponseEntity getResources(UsernamePasswordAuthenticationToken user) {
    Player player = (Player)user.getPrincipal();
    return ResponseEntity
        .status(200)
        .body(resourceService.convertToResourcesResDto(player.getKingdom().getResources()));
  }

}
