package com.greenfoxacademy.springwebapp.player.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "players")
public class Player {

    @Id
    private int id;
    @Column(length = 50)
    private String username;
    private Integer kingdomId;
    private String avatar;
    private Integer points;

}
