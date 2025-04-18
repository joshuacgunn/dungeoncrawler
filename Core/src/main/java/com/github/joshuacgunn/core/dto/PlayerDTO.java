package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.systems.PlayerStats;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {

    public Player.PlayerClass playerClass;

    public PlayerStats playerStats;


    public PlayerDTO() {
        super();
        this.setEntityType("Player");
    }

    public void setPlayerClass(Player.PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public Player.PlayerClass getPlayerClass() {
        return this.playerClass;
    }

    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }
}