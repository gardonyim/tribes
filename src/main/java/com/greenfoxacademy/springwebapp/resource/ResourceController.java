package com.greenfoxacademy.springwebapp.resource;

import com.greenfoxacademy.springwebapp.player.models.Player;
import com.greenfoxacademy.springwebapp.resource.models.ResourcesResDTO;
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
  public ResponseEntity registerUser(UsernamePasswordAuthenticationToken user) {
    Player player = (Player)user.getPrincipal();
    ResourcesResDTO resourcesResDTO = new ResourcesResDTO(
        resourceService.convertToResourceDtoList(player.getKingdom().getResources()));
    return ResponseEntity.status(201).body(resourcesResDTO);
  }

}
