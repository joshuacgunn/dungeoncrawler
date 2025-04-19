package com.github.joshuacgunn.core.dto;

import com.github.joshuacgunn.core.entity.Player;
import com.github.joshuacgunn.core.gameplay.GameState;
import com.github.joshuacgunn.core.mechanics.PlayerStats;

/**
 * PlayerDTO extends EntityDTO to add player-specific properties.
 */
public class PlayerDTO extends EntityDTO {
    public Player.PlayerClass playerClass;
    public PlayerStats playerStats;
    public String gameState;
    public String previousGameState;
    public int playerLevel;


    public PlayerDTO() {
        super();
        this.setEntityType("Player");
    }

    public int getPlayerLevel() {
        return this.playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public String getGameState() {
        return this.gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public String getPreviousGameState() {
        return this.previousGameState;
    }

    public void setPreviousGameState(String previousGameState) {
        this.previousGameState = previousGameState;
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